package net.ideahut.admin.central.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.filter.WebMvcRequestFilter;
import net.ideahut.springboot.helper.WebMvcHelper;

@Configuration
class FilterConfig {
	
	@Bean
	FilterRegistrationBean<WebMvcRequestFilter> defaultRequestFilter(
		Environment environment,
		AppProperties appProperties,
		RequestMappingHandlerMapping handlerMapping
	) {		
		return WebMvcHelper.createFilterBean(
			environment,
			new WebMvcRequestFilter()
				.setHandlerMapping(handlerMapping)
				.setCORSHeaders(appProperties.getCors())
				.setTraceEnable(true)
				.setEnableTimeResult(true)
				.initialize(), 
			1, 
			"/*"
		);
	}
	
}
