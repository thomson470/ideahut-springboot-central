package net.ideahut.admin.central.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.service.AdminService;
import net.ideahut.springboot.config.WebMvcBasicConfig;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.mapper.DataMapper;

@Configuration
@EnableWebMvc
class WebMvcConfig extends WebMvcBasicConfig {
	
	private final AppProperties appProperties;
	private final DataMapper dataMapper;
	private final AdminService adminService;
	private final HandlerInterceptor handlerInterceptor;
	
	@Autowired
	WebMvcConfig(
		AppProperties appProperties,
		DataMapper dataMapper,
		AdminService adminService,
		HandlerInterceptor handlerInterceptor
	) {
		this.appProperties = appProperties;
		this.dataMapper = dataMapper;
		this.adminService = adminService;
		this.handlerInterceptor = handlerInterceptor;
	}
	
	@Override
	protected String parameterName() {
		return "_fmt";
	}

	@Override
	protected boolean enableAcceptHeader() {
		return true;
	}

	@Override
	protected DataMapper dataMapper() {
		return dataMapper;
	}

	@Override
	protected HandlerInterceptor[] handlerInterceptors() {
		return new HandlerInterceptor[] {
			handlerInterceptor
		};
	}

	@Override
	protected boolean enableExtension() {
		return true;
	}
	
	@Override
	protected Map<String, MediaType> mediaTypes() {
		return new HashMap<>();
	}

	
	/*
	 * Resource Handlers
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		AppProperties.Web web = appProperties.getWeb();
		if (!registry.hasMappingForPattern(adminService.getWebPath() + "/**")) {
			registry
			.addResourceHandler(adminService.getWebPath() + "/**")
			.addResourceLocations("file:" + FrameworkHelper.replacePath(web.getLocation()))
			.setCacheControl(CacheControl.maxAge(60, TimeUnit.DAYS))
	        .resourceChain(false)
	        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/ui/**"));
		}
		AppProperties.Multimedia multimedia = appProperties.getMultimedia();
		if (!registry.hasMappingForPattern(multimedia.getPath() + "/**")) {
			registry
			.addResourceHandler(multimedia.getPath() + "/**")
			.addResourceLocations("file:" + FrameworkHelper.replacePath(multimedia.getLocation()));
		}
		super.addResourceHandlers(registry);
	}
	
}
