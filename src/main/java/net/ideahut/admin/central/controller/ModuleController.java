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
import net.ideahut.springboot.crud.CrudBuilder;
import net.ideahut.springboot.crud.CrudHandler;
import net.ideahut.springboot.crud.CrudResult;
import net.ideahut.springboot.entity.EntityInfo;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.TrxManagerInfo;
import net.ideahut.springboot.exception.ResultRuntimeException;
import net.ideahut.springboot.object.ImageScalr;
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
		byte[] iconBytes = null;
		if (file != null) {
			ImageScalr imageScalr = ImageScalr.of(file.getInputStream());
			iconBytes = ImageScalr.toByteArray(imageScalr.resize(480), imageScalr.getFormat());
			module.setIcon("/" + PATH + "/" + UUID.randomUUID().toString() + "." + imageScalr.getFormat().toLowerCase());
		}
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		CrudBuilder builder = CrudBuilder.of(crudHandler, trxManagerInfo, Module.class)
		.setValue(module);
		CrudResult result = builder.create();
		if (result.getError() != null) {
			throw ResultRuntimeException.of(Result.error(result.getError()));
		}
		if (iconBytes != null) {
			FileUtils.writeByteArrayToFile(new File(directory, module.getIcon().substring(PATH.length() + 1)), iconBytes);
		}
		return result.getValue();
	}
	
	
	@PostMapping(value = "/update")
	Module update(
		@ModelAttribute Module input,
		@RequestParam(name = "file", required = false) MultipartFile file
	) throws Exception {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		EntityInfo entityInfo = trxManagerInfo.getEntityInfo(Module.class);
		Module module = CrudBuilder.of(crudHandler, entityInfo)
		.setId(input.getModuleId()).unique();
		Assert.notNull(module, "Module not found");
		String oldIcon = null;
		String newIcon = null;
		byte[] iconBytes = null;
		if (file != null) {
			ImageScalr imageScalr = ImageScalr.of(file.getInputStream());
			iconBytes = ImageScalr.toByteArray(imageScalr.resize(480), imageScalr.getFormat());
			oldIcon = module.getIcon() != null ? module.getIcon() : null;
			newIcon = "/"+ PATH + "/" + UUID.randomUUID().toString() + "." + imageScalr.getFormat().toLowerCase();
		}
		if (entityInfo.merge(input, module, false, Arrays.asList("icon"))) {
			if (newIcon != null) {
				module.setIcon(newIcon);
			}
			CrudBuilder builder = CrudBuilder.of(crudHandler, trxManagerInfo, Module.class)
			.setId(input.getModuleId())
			.setValue(module);
			CrudResult result = builder.update();
			if (result.getError() != null) {
				throw ResultRuntimeException.of(Result.error(result.getError()));
			}
			if (iconBytes != null) {
				FileUtils.writeByteArrayToFile(new File(directory, module.getIcon().substring(PATH.length() + 1)), iconBytes);
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
	) {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		Module module = CrudBuilder.of(crudHandler, trxManagerInfo, Module.class)
		.setId(moduleId).delete();
		if (module != null && module.getIcon() != null) {
			FileUtils.deleteQuietly(new File(directory, module.getIcon().substring(PATH.length() + 1)));
		}
		return module;
	}
	
}
