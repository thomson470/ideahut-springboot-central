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

import com.fasterxml.jackson.databind.node.ObjectNode;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.mapper.DataMapper;

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
	        checkAdminFile(tmpdir); // check file yang diupload web admin atau tidak
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
	
	private static void checkAdminFile(File tmpdir) {
		File assetsDir = new File(tmpdir, "assets");
		ErrorHelper.throwIf(!assetsDir.isDirectory(), "Invalid assets");
		ErrorHelper.throwIf(!new File(tmpdir, "icons").isDirectory(), "Invalid icons");
		ErrorHelper.throwIf(!new File(tmpdir, "index.html").isFile(), "Invalid index.html");
		File indexJs = getAssetFile(Arrays.asList(assetsDir.listFiles()), "index", "js");
		ErrorHelper.throwIf(indexJs == null, "Index js not found");
		String content;
		try {
			content = FileUtils.readFileToString(indexJs, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw ErrorHelper.exception(e);
		}
		int idx = content.indexOf("\"" + ADMIN + "\":");
		if (idx == -1) {
			idx = content.indexOf(ADMIN + ":");
		}
		ErrorHelper.throwIf(idx == -1, "Invalid admin file");
		
	}
	
	static void prepareUI(AdminServiceImpl service) throws Exception {
		AppProperties.Web web = service.appProperties.getWeb();
		String location = FrameworkHelper.replacePath(web.getLocation());
		File directory = new File(location);
		if (directory.isDirectory()) {
			File assetsDir = new File(directory, "assets");
			if (assetsDir.isDirectory()) {
				List<File> assetFiles = Arrays.asList(assetsDir.listFiles());
				prepareIndexJs(service, assetFiles);
				prepareIndexHtml(service, directory);
			}
		}
	}
	
	private static void prepareIndexJs(
		AdminServiceImpl service,
		List<File> assetFiles
	) throws Exception {
		String webPath = "/";
		File indexJs = getAssetFile(assetFiles, "index", "js");
		if (indexJs != null && indexJs.isFile()) {
			AppProperties properties = service.appProperties;
			DataMapper mapper = service.dataMapper;
			String ltxt = FileUtils.readFileToString(indexJs, StandardCharsets.UTF_8);
			int idx  = indexOfApp(ltxt);
			if (idx != -1) {
				String rtxt = ltxt.substring(idx);
				ltxt = ltxt.substring(0, idx);
				idx = ltxt.lastIndexOf("={");
				ltxt = ltxt.substring(0, idx + 1);
				rtxt = "{" + rtxt;
				idx = rtxt.indexOf("},");
				String sapp = rtxt.substring(0, idx + 1);
				rtxt = rtxt.substring(idx + 1);
				int length = 5;
				idx = sapp.indexOf("web:\"");
				if (idx == -1) {
					length = 7;
					idx = sapp.indexOf("\"web\":\"");
				}
				if (idx != -1) {
					webPath = sapp.substring(idx + length);
					idx = webPath.indexOf("\"");
					webPath = webPath.substring(0, idx);
				}
				
				ObjectNode jnode = mapper.createObjectNode();
				ObjectNode japp = jnode.putObject(CENTRAL);
				
				AppProperties.Web web = properties.getWeb();
				
				String appId = FrameworkHelper.digest(FrameworkHelper.SHA1, service.applicationContext.getId() + "");
				japp.put("id", appId);
				
				String title = ObjectHelper.useOrDefault(web.getTitle(), "");
				title = ObjectHelper.useOrElse(
					!title.isEmpty(), 
					title, 
					service.applicationContext::getId
				);
				japp.put("title", title);
				
				japp.put("web", webPath);
				japp.put("api", "");
				
				Integer timeout = ObjectHelper.useOrDefault(web.getTimeout(), 60);
				timeout = ObjectHelper.useOrElse(timeout > 0, timeout, 60);
				japp.put("timeout", timeout);
				
				String language = ObjectHelper.useOrDefault(web.getLanguage(), "").trim();
				japp.put("language", language);
				
				Boolean debug = ObjectHelper.useOrDefault(web.getDebug(), Boolean.FALSE);
				japp.put("debug", debug);
				
				ltxt += mapper.writeAsString(jnode, DataMapper.JSON) + rtxt;
			}
			FileUtils.write(indexJs, ltxt, StandardCharsets.UTF_8);
		}
		service.webPath = webPath;
	}
	
	private static void prepareIndexHtml(
		AdminServiceImpl service,
		File directory
	) throws Exception {
		File indexHtml = new File(directory, "index.html");
		if (indexHtml.isFile()) {
			String content = FileUtils.readFileToString(indexHtml, StandardCharsets.UTF_8);
			AppProperties.Web web = service.appProperties.getWeb();
			String title = ObjectHelper.useOrDefault(web.getTitle(), "");
			title = ObjectHelper.useOrElse(
				!title.isEmpty(), 
				title, 
				service.applicationContext::getId
			);
			int idx = content.indexOf("<title>");
			if (idx != -1) {
				String text = content.substring(idx + 7);
				content = content.substring(0, idx + 7);
				idx = text.indexOf("</title>");
				text = text.substring(idx);
				content += title + text;
			}
			FileUtils.write(indexHtml, content, StandardCharsets.UTF_8);
		}
	}
	
	private static int indexOfApp(String ltxt) {
		int idx = ltxt.indexOf("\"" + CENTRAL + "\":");
		if (idx == -1) {
			idx = ltxt.indexOf(CENTRAL + ":");
		}
		return idx;
	}
	
	private static File getAssetFile(
		List<File> assetFiles,
		String prefix,
		String extention
	) {
		List<File> filter = ObjectHelper.filterList(assetFiles, f -> f.getName().startsWith(prefix + ".") && f.getName().endsWith("." + extention));
		List<File> files = ObjectHelper.callOrElse(
			filter.isEmpty(), 
			() -> ObjectHelper.filterList(assetFiles, f -> f.getName().startsWith(prefix + "-") && f.getName().endsWith("." + extention)), 
			() -> filter
		);
		return !files.isEmpty() ? files.get(0) : null;
	}

}
