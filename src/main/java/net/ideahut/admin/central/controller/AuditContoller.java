package net.ideahut.admin.central.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.audit.AuditRequest;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.WebMvcHelper;
import net.ideahut.springboot.object.Page;

@ComponentScan
@RestController
@RequestMapping("/audit")
class AuditContoller {
	
	private final AuditHandler auditHandler;
	
	@Autowired
	AuditContoller(
		AuditHandler auditHandler	
	) {
		this.auditHandler = auditHandler;
	}

	@GetMapping(value = "/info")
	AuditAccessible.AuditMember info(
		@RequestParam("type") String type
	) throws Exception {
		AuditAccessible accessible = auditHandler.getAccessibles().values().iterator().next();
		return accessible.getMembers().get(ObjectHelper.classOf(type));
	}
	
	@PostMapping(value = "/list")
	Page auditList(
		@RequestParam("handler") String handler
	) throws Exception {
		byte[] data = WebMvcHelper.getBodyAsBytes(WebMvcHelper.getRequest());
		AuditRequest request = auditHandler.getRequest(data);
		String entity = request.getEntity() != null ? request.getEntity().trim() : "";
		if (!entity.isEmpty()) {
			request.setClassOfEntity(ObjectHelper.classOf(entity));
		}
		return auditHandler.getList(request);
	}
	
}
