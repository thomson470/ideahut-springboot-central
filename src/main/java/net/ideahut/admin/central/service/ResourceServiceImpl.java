package net.ideahut.admin.central.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.util.FrameworkUtil;

@Service
public class ResourceServiceImpl implements ResourceService, InitializingBean {
	
	@Autowired
	private AppProperties appProperties;
	
	private byte[] bytes = new byte[0];
	private String version = System.currentTimeMillis() + "";

	@Override
	public void afterPropertiesSet() throws Exception {
		String location = appProperties.getAdminFile() != null ? FrameworkUtil.replacePath(appProperties.getAdminFile().trim()) : "";
		Path file =  Paths.get(location);
		BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
		bytes = Files.readAllBytes(file);
		version = attr.lastModifiedTime().toMillis() + "";
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public String getVersion() {
		return version;
	}

}
