package net.ideahut.admin.central.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.admin.AdminProperties;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.QuasarHelper;
import net.ideahut.springboot.helper.StringHelper;

final class AdminHelper {
	
	private AdminHelper() {}
	
	private static final String ADMIN = "__admin__";
	private static final String CENTRAL = "__central__";
	
	static void saveAdminFile(
		AdminServiceImpl service,	
		byte[] bytes
	) {
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
	        if (!QuasarHelper.isValidApp(ADMIN, tmpdir)) {
	        	// check file yang diupload web admin atau tidak
	        	throw new Exception("Invalid admin application, id: '" + ADMIN + "'");
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
	
	static void prepareUI(AdminServiceImpl service) throws Exception {
		AppProperties.Web web = ObjectHelper.useOrDefault(service.appProperties.getWeb(), AppProperties.Web::new);
		String location = FrameworkHelper.replacePath(web.getLocation());
		File directory = new File(location);
		if (!QuasarHelper.isValidApp(CENTRAL, directory)) {
			throw new Exception("Invalid central application, id: '" + CENTRAL + "'");
		}
		File assetsDir = new File(directory, "assets");
		List<File> assetFiles = Arrays.asList(assetsDir.listFiles());
		String webPath = ObjectHelper.useOrDefault(web.getPath(), "").trim();
		webPath = StringHelper.removeEnd(webPath, "/");
		String apiPath = "";
		String title = ObjectHelper.useOrDefault(web.getTitle(), "Central");
		AdminProperties.Web.Color color = service.dataMapper.convert(web.getColor(), AdminProperties.Web.Color.class);
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
			CENTRAL,
			properties, 
			apiPath,
			service.applicationContext, 
			assetFiles, 
			webPath, 
			title
		);
		QuasarHelper.prepareIndexCss(
			CENTRAL,
			properties, 
			assetFiles, 
			webPath
		);
		QuasarHelper.prepareIndexHtml(
			CENTRAL,
			directory, 
			webPath, 
			title
		);
		QuasarHelper.prepareBlankJs(
			CENTRAL,
			assetFiles, 
			webPath
		);
		prepareProjectJs(
			CENTRAL, 
			assetFiles, 
			webPath
		);
	}
	
	public static void prepareProjectJs(
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
