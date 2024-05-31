package net.ideahut.admin.central;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.redis.RedisProperties;
import net.ideahut.springboot.task.TaskProperties;
import net.ideahut.springboot.util.FrameworkUtil;
import net.ideahut.springboot.util.StringUtil;

@Configuration
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
public class AppProperties {
	
	private Boolean loggingError;
	private String adminFile;
	
	private Map<String, String> cors = new HashMap<>();
	private List<Class<?>> ignoredHandlerClasses = new ArrayList<>();
	
	private TaskProperties task = new TaskProperties();
	private RedisProperties redis = new RedisProperties();
	private Resource resource = new Resource();
	private Expiry expiry = new Expiry();
	private Multimedia multimedia = new Multimedia();
	private Grid grid = new Grid();
	
	
	@Setter
	@Getter
	public static class Resource {
		private String location;
		private String path;
		private String redirectParameter;
		private Set<String> allowedPaths;
	}
	
	@Setter
	@Getter
	public static class Expiry {
		private Integer auth;
		private Integer module;
		private Integer token;
	}
	
	@Setter
	@Getter
	public static class Grid {
		private String account;
		private String project;
		private String module;
		private String redirect;
	}
	
	@Setter
	@Getter
	public static class Multimedia {
		private String url;
		private String path;
		private String location;
		
		public File directory(String path) {
			String root = location != null ? FrameworkUtil.replacePath(location.trim()) : "";
			String child = path != null ? StringUtil.removeEnd(StringUtil.removeStart(path, "/"), "/") : "";
			File dir = new File(root, child);
			if (dir.exists() && !dir.isDirectory()) {
				throw FrameworkUtil.exception("Multimedia path " + dir.getAbsolutePath() + " is not a directory");
			}
			dir.mkdirs();
			dir.mkdir();
			return dir;
		}
	}
	
}
