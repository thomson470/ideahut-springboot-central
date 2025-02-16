package net.ideahut.admin.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import net.ideahut.admin.central.service.AdminService;
import net.ideahut.admin.central.service.TokenService;
import net.ideahut.springboot.annotation.Public;
import net.ideahut.springboot.security.SecurityUser;

@Public
@ComponentScan
@RestController
@RequestMapping("/sync")
class SyncContoller  {
	
	private AdminService adminService;
	private TokenService tokenService;
	
	@Autowired
	SyncContoller(
		AdminService adminService,
		TokenService tokenService	
	) {
		this.adminService = adminService;
		this.tokenService = tokenService;
	}
	@GetMapping(value = "/web")
	void web(
		@RequestParam(value = "version", required = false) String version,
		HttpServletResponse response
	) throws Exception  {
		if (adminService.getAdminVersion().equals(version)) {
			return;
		}
		response.setHeader("Admin-Version", adminService.getAdminVersion());
		if (adminService.isAdminRedirect()) {
			response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
			response.setHeader(HttpHeaders.LOCATION, adminService.getAdminRedirectUrl());
		} else {
			byte[] bytes = adminService.getAdminBytes();
			response.setContentLength(bytes.length);
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
		}
	}

	@RequestMapping(value = "/token", method = { RequestMethod.GET, RequestMethod.POST })
	SecurityUser token(
		@RequestParam("token") String token	
	) {
		return tokenService.getUser(token);
	}
	
}
