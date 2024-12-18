package net.ideahut.admin.central.controller;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.springboot.crud.CrudAction;
import net.ideahut.springboot.crud.CrudBuilder;
import net.ideahut.springboot.crud.CrudHandler;
import net.ideahut.springboot.crud.CrudResult;
import net.ideahut.springboot.entity.EntityInfo;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.TrxManagerInfo;
import net.ideahut.springboot.exception.ResultRuntimeException;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.object.Multimedia;
import net.ideahut.springboot.object.Result;

@ComponentScan
@RestController
@RequestMapping("/module")
class ModuleController implements InitializingBean {
	
	private static final String PATH = "module";
	
	private final AppProperties appProperties;
	private final EntityTrxManager entityTrxManager;
	private final CrudHandler crudHandler;
	
	private File directory;
	
	@Autowired
	ModuleController(
		AppProperties appProperties,
		EntityTrxManager entityTrxManager,
		CrudHandler crudHandler
	) {
		this.appProperties = appProperties;
		this.entityTrxManager = entityTrxManager;
		this.crudHandler = crudHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		AppProperties.Multimedia multimedia = appProperties.getMultimedia();
		directory = multimedia.directory(PATH);
	}
	
	@PostMapping("/create")
	Module create(
		@ModelAttribute Module module,
		@RequestParam(name = "file", required = false) MultipartFile file
	) throws Exception {
		Multimedia multimedia = getMultimedia(file);
		if (multimedia != null) {
			module.setIcon("/" + PATH + "/" + UUID.randomUUID().toString() + "." + multimedia.getExtention());
		}
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		CrudBuilder builder = CrudBuilder.of(trxManagerInfo, Module.class)
		.setValue(module);
		CrudResult result = builder.execute(crudHandler, CrudAction.CREATE);
		if (result.getError() != null) {
			throw ResultRuntimeException.of(Result.error(result.getError()));
		}
		if (multimedia != null) {
			FileUtils.writeByteArrayToFile(new File(directory, module.getIcon().substring(PATH.length() + 1)), multimedia.getBytes());
		}
		return result.getValue();
	}
	
	
	@PostMapping(value = "/update")
	Module update(
		@ModelAttribute Module input,
		@RequestParam(name = "file", required = false) MultipartFile file
	) throws Exception {
		Multimedia multimedia = getMultimedia(file);
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		EntityInfo entityInfo = trxManagerInfo.getEntityInfo(Module.class);
		Module module = CrudBuilder.of(entityInfo)
		.setId(input.getModuleId()).execute(crudHandler, CrudAction.UNIQUE).getValue();
		Assert.notNull(module, "Module not found");
		String oldIcon = null;
		String newIcon = null;
		if (multimedia != null) {
			oldIcon = module.getIcon() != null ? module.getIcon() : null;
			newIcon = "/"+ PATH + "/" + UUID.randomUUID().toString() + "." + multimedia.getExtention();
		}
		if (entityInfo.merge(input, module, false, Arrays.asList("icon"))) {
			if (newIcon != null) {
				module.setIcon(newIcon);
			}
			CrudBuilder builder = CrudBuilder.of(trxManagerInfo, Module.class)
			.setId(input.getModuleId())
			.setValue(module);
			CrudResult result = builder.execute(crudHandler, CrudAction.UPDATE);
			if (result.getError() != null) {
				throw ResultRuntimeException.of(Result.error(result.getError()));
			}
			if (multimedia != null) {
				FileUtils.writeByteArrayToFile(new File(directory, module.getIcon().substring(PATH.length() + 1)), multimedia.getBytes());
				if (oldIcon != null) {
					FileUtils.deleteQuietly(new File(directory, oldIcon.substring(PATH.length() + 1)));
				}
			}
			return result.getValue();
		}
		return module;
	}
	
	@DeleteMapping(value = "/delete")
	Module delete(
		@RequestParam("moduleId") String moduleId
	) throws Exception {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		Module module = CrudBuilder.of(trxManagerInfo, Module.class)
		.setId(moduleId).execute(crudHandler, CrudAction.DELETE).getValue();
		if (module != null && module.getIcon() != null) {
			FileUtils.deleteQuietly(new File(directory, module.getIcon().substring(PATH.length() + 1)));
		}
		return module;
	}

	
	private Multimedia getMultimedia(MultipartFile file) throws Exception {
		Multimedia multimedia = null;
		if (file != null) {
			multimedia = Multimedia.of(file.getBytes()).setWidth(480);
			if (Multimedia.IMAGE != multimedia.getType()) {
				throw ErrorHelper.exception("Invalid image");
			}
		}
		return multimedia;
	}
	
	
}
