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
import net.ideahut.admin.central.service.AdminService;
import net.ideahut.springboot.audit.AuditInfo;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.WebMvcHelper;

@Component
@ComponentScan
class RequestInterceptor implements HandlerInterceptor {
	
	private static class Strings {
		private static final String SLASH = new StringBuilder("/").toString();
	}
	
	private final AppProperties appProperties;
	private final AdminService adminService;
	private final AccessService accessService;
	
	@Autowired
	RequestInterceptor(
		AppProperties appProperties,
		AdminService adminService,
		AccessService accessService	
	) {
		this.appProperties = appProperties;
		this.adminService = adminService;
		this.accessService = accessService;
	}
	
	@Override
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler
	) throws Exception {
		if (!Application.isReady()) {
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return false;
		}
		if (ObjectHelper.isInstance(HandlerMethod.class, handler)) {
			return handleApi(request, response, (HandlerMethod) handler);
		}
		else if (ObjectHelper.isInstance(ResourceHttpRequestHandler.class, handler)) {
			return handleWeb(request, response);
		}
		return true;
	}
	
	private boolean handleApi(
		HttpServletRequest request, 
		HttpServletResponse response,
		HandlerMethod handlerMethod
	) {
		AuditInfo.context().setAuditor("_PUBLIC_");
		Access access = null;
		String authorization = WebMvcHelper.getHeader(request, HttpHeaders.AUTHORIZATION, "");
		if (!authorization.isEmpty()) {
			access = accessService.info(authorization);
		}
		if (access == null) {
			access = Access.of(authorization, null);
		}
		boolean isPublic = FrameworkHelper.isPublic(handlerMethod);
		if (!isPublic && access.getAccount() == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		if (!appProperties.getIgnoredHandlerClasses().contains(handlerMethod.getBeanType())) {
			AuditInfo.context().setInfo(
				handlerMethod.getBeanType().getName() + 
				":" + 
				handlerMethod.getMethod().getName() + 
				"(" + 
				handlerMethod.getMethod().getParameterCount() + 
				")"
			);				
		}
		if (access.getAccount() != null) {
			AuditInfo.context().setAuditor(access.getAccount().getUsername());
		}
		Access.set(access);
		return true;
	}
	
	private boolean handleWeb(
		HttpServletRequest request, 
		HttpServletResponse response
	) throws Exception {
		String webPath = adminService.getWebPath() + Strings.SLASH;
		String servletPath = request.getServletPath();
		if (servletPath.length() < webPath.length() && !servletPath.endsWith(Strings.SLASH)) {
			servletPath += Strings.SLASH;
		}
		if (servletPath.startsWith(webPath)) {
			String queryString = request.getQueryString() != null && !request.getQueryString().isEmpty() ? ("?" + request.getQueryString()) : "";
			String nextPath = servletPath.substring(webPath.length());
			if (nextPath.isEmpty()) {
				response.sendRedirect(webPath + "index.html" + queryString);
				return false;
			}
			else if ("index.html".equals(nextPath)) {
				return true;
			}
			if (!isAllowedWebRequest(request, response, webPath, nextPath, queryString)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isAllowedWebRequest(
		HttpServletRequest request, 
		HttpServletResponse response,
		String webPath,
		String nextPath,
		String queryString
	) throws Exception {
		AppProperties.Web web = appProperties.getWeb();
		String allowedPath = nextPath;
		int idx = nextPath.indexOf(Strings.SLASH);
		if (idx != -1) {
			allowedPath = nextPath.substring(0, idx);
		}
		if (!web.getAllowedPaths().contains(allowedPath)) {
			String redirect = request.getParameter(web.getRedirectParameter());
			if (redirect == null) {
				redirect = "/" + nextPath + queryString;
				redirect = Base64.getEncoder().encodeToString(redirect.getBytes());
				try {
					redirect = URLEncoder.encode(redirect, StandardCharsets.UTF_8.name());
				} catch (Exception e) { /**/ }
				response.sendRedirect(webPath + "index.html?" + web.getRedirectParameter() + "=" + redirect);
				return false;
			}
		}
		return true;
	}
	
}
