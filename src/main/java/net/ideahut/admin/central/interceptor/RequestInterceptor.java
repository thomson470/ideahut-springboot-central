package net.ideahut.admin.central.interceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.Application;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.service.AccessService;
import net.ideahut.springboot.annotation.Public;
import net.ideahut.springboot.audit.AuditInfo;
import net.ideahut.springboot.util.FrameworkUtil;
import net.ideahut.springboot.util.WebMvcUtil;

@Component
@ComponentScan
public class RequestInterceptor implements HandlerInterceptor {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private AccessService accessService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)	throws Exception {
		if (!Application.isReady()) {
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return false;
		}
		String servletPath = request.getServletPath();
		if (handler instanceof HandlerMethod) {
			AuditInfo.context().setAuditor("_PUBLIC_");
			HandlerMethod hm = (HandlerMethod) handler;
			Access access = null;
			String authorization = WebMvcUtil.getHeader(request, HttpHeaders.AUTHORIZATION, "");
			if (!authorization.isEmpty()) {
				access = accessService.info(authorization);
			}
			if (access == null) {
				access = Access.of(authorization, null);
			}
			Public anPublic = FrameworkUtil.getAnnotation(Public.class, hm);
			boolean isPublic = anPublic != null && anPublic.value();
			if (!isPublic && access.getAccount() == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
			if (!appProperties.getIgnoredHandlerClasses().contains(hm.getBeanType())) {
				AuditInfo.context().setInfo(hm.getBeanType().getName() + ":" + hm.getMethod().getName() + "(" + hm.getMethod().getParameterCount() + ")");				
			}
			if (access.getAccount() != null) {
				AuditInfo.context().setAuditor(access.getAccount().getUsername());
			}
			Access.set(access);
		}
		else if (handler instanceof ResourceHttpRequestHandler) {
			AppProperties.Resource resource = appProperties.getResource();
			String resourcePath = resource.getPath() + "/";
			if (servletPath.length() < resourcePath.length() && !servletPath.endsWith("/")) {
				servletPath += "/";
			}
			if (servletPath.startsWith(resourcePath)) {
				String queryString = request.getQueryString() != null && !request.getQueryString().isEmpty() ? ("?" + request.getQueryString()) : "";
				String nextPath = servletPath.substring(resourcePath.length());
				if (nextPath.isEmpty()) {
					response.sendRedirect(resourcePath + "index.html" + queryString);
					return false;
				}
				else if ("index.html".equals(nextPath)) {
					return true;
				}
				String allowedPath = nextPath;
				int idx = nextPath.indexOf("/");
				if (idx != -1) {
					allowedPath = nextPath.substring(0, idx);
				}
				if (!resource.getAllowedPaths().contains(allowedPath)) {
					String redirect = request.getParameter(resource.getRedirectParameter());
					if (redirect == null) {
						redirect = "/" + nextPath + queryString;
						redirect = Base64.getEncoder().encodeToString(redirect.getBytes());
						try {
							redirect = URLEncoder.encode(redirect, StandardCharsets.UTF_8.name());
						} catch (Exception e) {}
						response.sendRedirect(resourcePath + "index.html?" + resource.getRedirectParameter() + "=" + redirect);
						return false;
					}
				}
			}
		}
		return true;
	}	
	
}
