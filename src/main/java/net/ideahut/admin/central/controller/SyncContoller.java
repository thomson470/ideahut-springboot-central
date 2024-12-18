package net.ideahut.admin.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
	ResponseEntity<StreamingResponseBody> web(
		@RequestParam(value = "version", required = false) String version	
	) throws Exception {
		if (adminService.getAdminVersion().equals(version)) {
			return ResponseEntity.ok().build();
		}
		StreamingResponseBody body = response -> response.write(adminService.getAdminBytes());
		return ResponseEntity.ok()
		.header("Admin-Version", adminService.getAdminVersion())
		.contentType(MediaType.APPLICATION_OCTET_STREAM)
		.body(body);
	}

	@RequestMapping(value = "/token", method = { RequestMethod.GET, RequestMethod.POST })
	SecurityUser token(
		@RequestParam("token") String token	
	) throws Exception {
		return tokenService.getUser(token);
	}
	
}
