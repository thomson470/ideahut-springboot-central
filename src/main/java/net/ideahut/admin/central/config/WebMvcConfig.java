package net.ideahut.admin.central.config;

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
import net.ideahut.admin.central.interceptor.RequestInterceptor;
import net.ideahut.springboot.config.WebMvcBasicConfig;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.util.FrameworkUtil;

@Configuration
@EnableWebMvc
class WebMvcConfig extends WebMvcBasicConfig {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private DataMapper dataMapper;
	@Autowired
	private RequestInterceptor requestInterceptor;
	
	
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
			requestInterceptor
		};
	}

	@Override
	protected boolean enableExtension() {
		return true;
	}
	
	@Override
	protected Map<String, MediaType> mediaTypes() {
		return null;
	}

	
	/*
	 * Resource Handlers
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		AppProperties.Resource resource = appProperties.getResource();
		if (!registry.hasMappingForPattern(resource.getPath() + "/**")) {
			registry
			.addResourceHandler(resource.getPath() + "/**")
			.addResourceLocations("file:" + FrameworkUtil.replacePath(resource.getLocation()))
			.setCacheControl(CacheControl.maxAge(60, TimeUnit.DAYS))
	        .resourceChain(false)
	        .addResolver(new VersionResourceResolver().addContentVersionStrategy("/ui/**"));
		}
		AppProperties.Multimedia multimedia = appProperties.getMultimedia();
		if (!registry.hasMappingForPattern(multimedia.getPath() + "/**")) {
			registry
			.addResourceHandler(multimedia.getPath() + "/**")
			.addResourceLocations("file:" + FrameworkUtil.replacePath(multimedia.getLocation()));
		}
		super.addResourceHandlers(registry);
	}
	
}
