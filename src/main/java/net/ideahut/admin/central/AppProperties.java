package net.ideahut.admin.central;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.definition.DatabaseAuditDefinition;
import net.ideahut.springboot.definition.FilterDefinition;
import net.ideahut.springboot.definition.FuryDefinition;
import net.ideahut.springboot.definition.KryoDefinition;
import net.ideahut.springboot.entity.EntityForeignKeyParam;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.StringHelper;
import net.ideahut.springboot.object.StorageKeyParam;
import net.ideahut.springboot.object.TimeValue;
import net.ideahut.springboot.redis.RedisProperties;
import net.ideahut.springboot.task.TaskProperties;

@Configuration
@ConfigurationProperties(prefix = "config")
@Setter
@Getter
public class AppProperties {
	
	private Boolean waitAllBeanConfigured;
	private Boolean logAllError;
	private String binarySerializer;
	private Properties datasource;
	private Properties hibernate;
	private EntityForeignKeyParam foreignKey;
	private DatabaseAuditDefinition audit;
	private FilterDefinition filter;
	private Expiry expiry;
	private TaskProperties task;
	private Web web;
	private Multimedia multimedia;
	private Grid grid;
	private Redis redis;
	private Binary binary;
	private AdminFile adminFile;
	
	
	@Setter
	@Getter
	public static class Web {
		private String path;
		private String title;
		private String location;
		private String redirectParameter;
		private Set<String> allowedPaths;
		private Integer timeout;
		private String language;
		private Boolean debug;
		private Color color;
		private TimeValue cacheMaxAge;
		private Boolean resourceChain;
	}
	
	@Getter
	@Setter
	public static class Color {
		private String header;
		private String title;
		private String primary;
		private String secondary;
		private String accent;
		private String dark;
		private String positive;
		private String negative;
		private String info;
		private String warning;
	}
	
	@Setter
	@Getter
	public static class Expiry {
		private TimeValue auth;
		private TimeValue module;
		private TimeValue token;
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
			String root = location != null ? FrameworkHelper.replacePath(location.trim()) : "";
			String child = path != null ? StringHelper.removeEnd(StringHelper.removeStart(path, "/"), "/") : "";
			File dir = new File(root, child);
			if (dir.exists() && !dir.isDirectory()) {
				throw ErrorHelper.exception("Multimedia path " + dir.getAbsolutePath() + " is not a directory");
			}
			dir.mkdirs();
			dir.mkdir();
			return dir;
		}
	}
	
	@Setter
	@Getter
	public static class Redis {
		private StorageKeyParam storageKeyParam;
		private RedisProperties.Connection connection;
	}
	
	@Getter
	@Setter
	public static class Binary {
		private KryoDefinition kryo;
		private FuryDefinition fury;
	}
	
	@Getter
	@Setter
	public static class AdminFile {
		private Boolean redirect;
		private String name;
	}
	
}
