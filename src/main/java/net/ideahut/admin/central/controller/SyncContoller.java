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

import net.ideahut.admin.central.service.ResourceService;
import net.ideahut.admin.central.service.TokenService;
import net.ideahut.springboot.annotation.Public;
import net.ideahut.springboot.security.SecurityUser;

@Public
@ComponentScan
@RestController
@RequestMapping("/sync")
class SyncContoller  {
	
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private TokenService tokenService;
	
	@GetMapping(value = "/resource")
	protected ResponseEntity<StreamingResponseBody> admin(
		@RequestParam(value = "version", required = false) String version	
	) throws Exception {
		if (resourceService.getVersion().equals(version)) {
			return ResponseEntity.ok().build();
		}
		StreamingResponseBody body = response -> {
			response.write(resourceService.getBytes());
		};
		return ResponseEntity.ok()
		.header("Admin-Version", resourceService.getVersion())
		.contentType(MediaType.APPLICATION_OCTET_STREAM)
		.body(body);
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/token")
	protected SecurityUser token(
		@RequestParam("token") String token	
	) throws Exception {
		return tokenService.getUser(token);
	}
	
}
