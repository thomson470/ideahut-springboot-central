package net.ideahut.admin.central.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.service.AdminService;
import net.ideahut.springboot.object.Multimedia;
import net.ideahut.springboot.object.Option;
import net.ideahut.springboot.util.FrameworkUtil;

@ComponentScan
@RestController
@RequestMapping("/tool")
class ToolController implements InitializingBean {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private AdminService adminService;
	
	private List<Option> iconTypes = new ArrayList<>();
	private Map<String, File> directories = new HashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		iconTypes.clear();
		iconTypes.add(new Option("project", "Project"));
		iconTypes.add(new Option("module", "Module"));
		for (Option iconType : iconTypes) {
			File directory = appProperties.getMultimedia().directory(iconType.getValue());
			directories.put(iconType.getValue(), directory);
		}
	}
	
	/*
	 * ICON SYNC
	 */
	@PostMapping(value = "/icon/sync")
	protected void iconSync(
		@RequestParam("password") String password
	) {
		Account account = FrameworkUtil.getOrDefault(Access.get().getAccount(), new Account());
		FrameworkUtil.throwIf(!FrameworkUtil.isTrue(account.getEnableImageUpload()), "User is not allowed");
		FrameworkUtil.throwIf(account.getPassword() == null, "Invalid account");
		FrameworkUtil.throwIf(!BCrypt.checkpw(password, account.getPassword()), "Invalid password");
		adminService.iconSync();
	}
	
	/*
	 * ICON OPTIONS
	 */
	@GetMapping(value = "/icon/types")
	protected List<Option> iconTypes() {
		return iconTypes;
	}

	/*
	 * ICON UPLOAD
	 */
	@PostMapping("/icon/upload")
	protected String iconUpload(
		@RequestParam("type") String type,
		@RequestParam("file") MultipartFile file
	) throws Exception {
		Account account = FrameworkUtil.getOrDefault(Access.get().getAccount(), new Account());
		FrameworkUtil.throwIf(!FrameworkUtil.isTrue(account.getEnableImageUpload()), "User is not allowed");
		File directory = directories.get(type);
		FrameworkUtil.throwIf(directory == null, "Invalid type: " + type);
		Multimedia multimedia = Multimedia.of(file.getBytes()).setWidth(480);
		if (Multimedia.IMAGE != multimedia.getType()) {
			throw FrameworkUtil.exception("Invalid image");
		}
		String icon = UUID.randomUUID().toString() + "." + multimedia.getExtention();
		FileUtils.writeByteArrayToFile(new File(directory, icon), multimedia.getBytes());
		return "/"+ type + "/" + icon;
	}
	
	/*
	 * BCRYPT GENERATE
	 */
	@PostMapping("/bcrypt/generate")
	protected String bcryptGenerate(
		@RequestParam(name = "round", required = false) Integer round,
		@RequestParam("password") String password
	) throws Exception {
		String salt;
		if (round != null && round > 0) {
			salt = BCrypt.gensalt(round);
		} else {
			salt = BCrypt.gensalt();
		}
		return BCrypt.hashpw(password, salt);
	}
	
	/*
	 * BCRYPT CHECK
	 */
	@PostMapping("/bcrypt/check")
	protected Boolean bcryptCheck(
		@RequestParam("password") String password,
		@RequestParam("hash") String hash
	) throws Exception {
		return BCrypt.checkpw(password, hash);
	}
	
	/*
	 * PASSWORD ENCRYPT
	 */
	@PostMapping("/password/encrypt")
	protected String passwordEncrypt(
		@RequestParam(name = "factor", required = false) Integer factor,
		@RequestParam("password") String password
	) throws Exception {
		return FrameworkUtil.encryptToBase64(password, factor);
	}
	
}
