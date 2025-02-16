package net.ideahut.admin.central.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.data.redis.core.ValueOperations;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.admin.AdminProperties;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.QuasarHelper;
import net.ideahut.springboot.helper.StringHelper;

final class AdminHelper {
	
	private AdminHelper() {}
	
	private static final class AppId {
		private static final String ADMIN = "__admin__";
		private static final String CENTRAL = "__central__";
	}
	
	private static final class RedisKey {
		static String lock(String redisPrefix) {
			return redisPrefix + "ADMIN***LOCK";
		}
		static String version(String redisPrefix) {
			return redisPrefix + "ADMIN-VERSION";
		}
		static String bytes(String redisPrefix) {
			return redisPrefix + "ADMIN-BYTES";
		}
	}
	
	/*
	 * LOCK
	 */
	static synchronized boolean lock(AdminServiceImpl service) {
		String key = RedisKey.lock(service.redis.getPrefix());
		ValueOperations<String, byte[]> valops = service.redis.getTemplate().opsForValue();
		byte[] bytes = valops.get(key);
		if (bytes != null) {
			return false;
		}
		valops.set(key, "1".getBytes());
		return true;
	}
	
	/*
	 * UNLOCK
	 */
	static void unlock(AdminServiceImpl service) {
		String key = RedisKey.lock(service.redis.getPrefix());
		service.redis.getTemplate().delete(key);
	}
	
	/*
	 * CLEAR
	 */
	static void clear(AdminServiceImpl service) {
		String prefix = service.redis.getPrefix();
		service.redis.getTemplate().delete(Arrays.asList(
			RedisKey.version(prefix),
			RedisKey.bytes(prefix)
		));
	}
	
	/*
	 * RELOAD
	 */
	static void reload(AdminServiceImpl service) throws Exception {
		File file = service.adminFile.toFile();
		if (file.isFile()) {
			BasicFileAttributes attr = Files.readAttributes(service.adminFile, BasicFileAttributes.class);
			String version = attr.lastModifiedTime().toMillis() + "";
			String prefix = service.redis.getPrefix();
			ValueOperations<String, byte[]> valops = service.redis.getTemplate().opsForValue();
			if (!service.isAdminRedirect()) {
				byte[] bytes = Files.readAllBytes(service.adminFile);
				valops.set(RedisKey.bytes(prefix), bytes);
			}
			valops.set(RedisKey.version(prefix), version.getBytes());
		}
	}
	
	/*
	 * VERSION
	 */
	static String version(AdminServiceImpl service) {
		byte[] value = service.redis.getTemplate().opsForValue().get(RedisKey.version(service.redis.getPrefix()));
		return value != null ? new String(value) : "";
	}
	
	/*
	 * BYTES
	 */
	static byte[] bytes(AdminServiceImpl service) {
		byte[] value = service.redis.getTemplate().opsForValue().get(RedisKey.bytes(service.redis.getPrefix()));
		return value != null ? value : ObjectHelper.emptyBinary();
	}
	
	/*
	 * SAVE
	 */
	static void save(
		AdminServiceImpl service,	
		byte[] bytes
	) {
		String appId = AppId.ADMIN;
		File adminFile = service.adminFile.toFile();
		File tmpdir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString().replace("-", ""));
		tmpdir.mkdirs();
		try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes))) {
			ZipEntry zen = zis.getNextEntry();
			byte[] buffer = new byte[1024];
			while (zen != null) {
	        	File destFile = new File(tmpdir, zen.getName());
	        	if (zen.isDirectory()) {
	        		ErrorHelper.throwIf(!destFile.isDirectory() && !destFile.mkdirs(), "Failed to create directory: {}", destFile);
	        	} else {
	        		File parent = destFile.getParentFile();
	        		ErrorHelper.throwIf(!parent.isDirectory() && !parent.mkdirs(), "Failed to create directory: {}", parent);
	                try (FileOutputStream fos = new FileOutputStream(destFile)) {
	                	int len;
		                while ((len = zis.read(buffer)) > 0) {
		                    fos.write(buffer, 0, len);
		                }
		                fos.flush();
	                }
	        	}
	        	zen = zis.getNextEntry();
	        }
	        zis.closeEntry();
	        if (!QuasarHelper.isValidApp(appId, tmpdir)) {
	        	// check file yang diupload web admin atau tidak
	        	throw new Exception("Invalid admin application, id: '" + appId + "'");
	        }
	        FileUtils.deleteQuietly(adminFile);
	        FileUtils.writeByteArrayToFile(adminFile, bytes);
		} catch (Exception e) {
			throw ErrorHelper.exception(e);
		} finally {
			FileUtils.deleteQuietly(tmpdir);
		}
		try {
			service.onReloadBean();
		} catch (Exception e) {
			throw ErrorHelper.exception(e);
		}
	}
	
	/*
	 * PREPARE UI
	 */
	static void prepareUI(AdminServiceImpl service) throws Exception {
		String appId = AppId.CENTRAL;
		AppProperties.Web web = ObjectHelper.useOrDefault(service.appProperties.getWeb(), AppProperties.Web::new);
		String location = FrameworkHelper.replacePath(web.getLocation());
		File directory = new File(location);
		if (!QuasarHelper.isValidApp(appId, directory)) {
			throw new Exception("Invalid central application, id: '" + appId + "'");
		}
		File assetsDir = new File(directory, "assets");
		List<File> assetFiles = Arrays.asList(assetsDir.listFiles());
		String webPath = ObjectHelper.useOrDefault(web.getPath(), "").trim();
		webPath = StringHelper.removeEnd(webPath, "/");
		String apiPath = "";
		String title = ObjectHelper.useOrDefault(web.getTitle(), "Central");
		AdminProperties.Web.Color color = FrameworkHelper.defaultDataMapper().convert(web.getColor(), AdminProperties.Web.Color.class);
		AdminProperties properties = new AdminProperties()
		.setApi(
			new AdminProperties.Api()
			.setDebug(web.getDebug())
			.setLanguage("en")
			.setTimeout(web.getTimeout())
		)
		.setWeb(
			new AdminProperties.Web()
			.setAllowedPaths(null)
			.setAlwaysToIndex(null)
			.setColor(color)
			.setIndexFile(title)
			.setRedirectParameter(title)
			.setTitle(title)
		);
		QuasarHelper.prepareIndexJs(
			appId,
			properties, 
			apiPath,
			service.applicationContext, 
			assetFiles, 
			webPath, 
			title
		);
		QuasarHelper.prepareIndexCss(
			appId,
			properties, 
			assetFiles, 
			webPath
		);
		QuasarHelper.prepareIndexHtml(
			appId,
			directory, 
			webPath, 
			title
		);
		QuasarHelper.prepareBlankJs(
			appId,
			assetFiles, 
			webPath
		);
		prepareProjectJs(
			appId, 
			assetFiles, 
			webPath
		);
	}
	
	/*
	 * PREPARE PROJECT JS
	 */
	private static void prepareProjectJs(
		String appId,
		List<File> assetFiles,
		String webPath
	) throws Exception {
		File projectJs = QuasarHelper.getAssetFile(assetFiles, "Project", "js");
		if (projectJs != null && projectJs.isFile()) {
			QuasarHelper.backup(projectJs);
			String content = FileUtils.readFileToString(projectJs, StandardCharsets.UTF_8);
			content = content.replace("/" + appId + "/", webPath + "/");
			FileUtils.write(projectJs, content, StandardCharsets.UTF_8);
		}
	}

}
