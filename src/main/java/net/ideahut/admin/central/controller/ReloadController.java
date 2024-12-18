package net.ideahut.admin.central.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ideahut.admin.central.service.ReloadService;

@ComponentScan
@RestController
@RequestMapping("/reload")
class ReloadController {

	private final ReloadService reloadService;
	
	@Autowired
	ReloadController(
		ReloadService reloadService		
	) {
		this.reloadService = reloadService;
	}
	
	@GetMapping("/names")
	Set<String> names() {
		return reloadService.names();
	}
	
	@PostMapping("/now")
	Boolean now(
		@RequestParam("name") String name	
	) throws Exception {
		return reloadService.reload(name);
	}
	
}
