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
import net.ideahut.springboot.config.WebMvcBasicConfig;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.StringHelper;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.object.TimeValue;

@Configuration
@EnableWebMvc
class WebMvcConfig extends WebMvcBasicConfig {
	
	private final AppProperties appProperties;
	private final DataMapper dataMapper;
	private final HandlerInterceptor handlerInterceptor;
	
	@Autowired
	WebMvcConfig(
		AppProperties appProperties,
		DataMapper dataMapper,
		HandlerInterceptor handlerInterceptor
	) {
		this.appProperties = appProperties;
		this.dataMapper = dataMapper;
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
		AppProperties.Web web = ObjectHelper.useOrDefault(appProperties.getWeb(), AppProperties.Web::new);
		String path = ObjectHelper.useOrDefault(web.getPath(), "").trim();
		path = StringHelper.removeEnd(path, "/");
		if (!registry.hasMappingForPattern(path + "/**")) {
			TimeValue cacheMaxAge = ObjectHelper.useOrDefault(web.getCacheMaxAge(), () -> TimeValue.of(TimeUnit.MINUTES, 60L));
			registry
			.addResourceHandler(path + "/**")
			.addResourceLocations("file:" + FrameworkHelper.replacePath(web.getLocation()))
			.setCacheControl(CacheControl.maxAge(cacheMaxAge.getValue(), cacheMaxAge.getUnit()))
	        .resourceChain(Boolean.TRUE.equals(web.getResourceChain()))
	        .addResolver(new VersionResourceResolver().addContentVersionStrategy(path + "/**"));
		}
		AppProperties.Multimedia multimedia = ObjectHelper.useOrDefault(appProperties.getMultimedia(), AppProperties.Multimedia::new);
		if (!registry.hasMappingForPattern(multimedia.getPath() + "/**")) {
			registry
			.addResourceHandler(multimedia.getPath() + "/**")
			.addResourceLocations("file:" + FrameworkHelper.replacePath(multimedia.getLocation()));
		}
		super.addResourceHandlers(registry);
	}
	
}
