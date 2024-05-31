package net.ideahut.admin.central.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.filter.WebMvcRequestFilter;
import net.ideahut.springboot.util.FrameworkUtil;

/*
 * Konfigurasi Filter
 */
@Configuration
class FilterConfig {
	
	@Autowired
	private Environment environment;
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;
	
	@Bean
	protected FilterRegistrationBean<WebMvcRequestFilter> defaultRequestFilter() {		
		return FrameworkUtil.createFilterBean(
			environment,
			new WebMvcRequestFilter()
				.setHandlerMapping(handlerMapping)
				.setCorsHeaders(appProperties.getCors())
				.setTraceEnable(true)
				.initialize(), 
			1, 
			"/*"
		);
	}
	
}
