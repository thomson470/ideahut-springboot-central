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
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.object.Multimedia;
import net.ideahut.springboot.object.Option;
import net.ideahut.springboot.object.Result;

@ComponentScan
@RestController
@RequestMapping("/tool")
class ToolController implements InitializingBean {
	
	private static class Strings {
		private static final String USER_NOT_ALLOWED = "User not allowed";
	}
	
	private final AppProperties appProperties;
	private final AdminService adminService;
	
	private List<Option> imageTypes = new ArrayList<>();
	private Map<String, File> directories = new HashMap<>();
	
	@Autowired
	ToolController(
		AppProperties appProperties,
		AdminService adminService
	) {
		this.appProperties = appProperties;
		this.adminService = adminService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		imageTypes.clear();
		imageTypes.add(new Option("project", "Project"));
		imageTypes.add(new Option("module", "Module"));
		for (Option imageType : imageTypes) {
			File directory = appProperties.getMultimedia().directory(imageType.getValue());
			directories.put(imageType.getValue(), directory);
		}
	}
	
	/*
	 * IMAGE SYNC
	 */
	@PostMapping(value = "/image/sync")
	void imageSync(
		@RequestParam("password") String password
	) {
		Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
		ErrorHelper.throwIf(!FrameworkHelper.isTrue(account.getEnableImageUpload()), Strings.USER_NOT_ALLOWED);
		ErrorHelper.throwIf(account.getPassword() == null, "Invalid account");
		ErrorHelper.throwIf(!BCrypt.checkpw(password, account.getPassword()), "Invalid password");
		adminService.syncImages();
	}
	
	/*
	 * IMAGE TYPES
	 */
	@GetMapping(value = "/image/types")
	List<Option> imageTypes() {
		return imageTypes;
	}

	/*
	 * IMAGE UPLOAD
	 */
	@PostMapping("/image/upload")
	String imageUpload(
		@RequestParam("type") String type,
		@RequestParam("file") MultipartFile file
	) throws Exception {
		Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
		ErrorHelper.throwIf(!FrameworkHelper.isTrue(account.getEnableImageUpload()), Strings.USER_NOT_ALLOWED);
		File directory = directories.get(type);
		ErrorHelper.throwIf(directory == null, "Invalid type: " + type);
		Multimedia multimedia = Multimedia.of(file.getBytes()).setWidth(480);
		if (Multimedia.IMAGE != multimedia.getType()) {
			throw ErrorHelper.exception("Invalid image");
		}
		String image = UUID.randomUUID().toString() + "." + multimedia.getExtention();
		FileUtils.writeByteArrayToFile(new File(directory, image), multimedia.getBytes());
		return "/"+ type + "/" + image;
	}
	
	/*
	 * ADMIN UPLOAD
	 */
	@PostMapping("/admin/upload")
	Result adminUpload(
		@RequestParam("file") MultipartFile file
	) throws Exception {
		Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
		ErrorHelper.throwIf(!FrameworkHelper.isTrue(account.getEnableAdminUpload()), Strings.USER_NOT_ALLOWED);
		String oldVersion = adminService.getAdminVersion();
		Integer oldSize = adminService.getAdminBytes().length;
		adminService.saveAdmin(file.getBytes());
		String newVersion = adminService.getAdminVersion();
		Integer newSize = adminService.getAdminBytes().length;
		return Result.success()
		.setInfo("oldVersion", oldVersion)
		.setInfo("oldSize", oldSize + "")
		.setInfo("newVersion", newVersion)
		.setInfo("newSize", newSize + "");
	}
	
	/*
	 * BCRYPT GENERATE
	 */
	@PostMapping("/bcrypt/generate")
	String bcryptGenerate(
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
	Boolean bcryptCheck(
		@RequestParam("password") String password,
		@RequestParam("hash") String hash
	) throws Exception {
		return BCrypt.checkpw(password, hash);
	}
	
	/*
	 * PASSWORD ENCRYPT
	 */
	@PostMapping("/password/encrypt")
	String passwordEncrypt(
		@RequestParam(name = "factor", required = false) Integer factor,
		@RequestParam("password") String password
	) throws Exception {
		return FrameworkHelper.encryptToBase64(password, factor);
	}
	
}
