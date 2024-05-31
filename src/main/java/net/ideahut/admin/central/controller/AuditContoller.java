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
import net.ideahut.springboot.object.Page;
import net.ideahut.springboot.util.FrameworkUtil;
import net.ideahut.springboot.util.WebMvcUtil;

@ComponentScan
@RestController
@RequestMapping("/audit")
class AuditContoller {
	
	@Autowired
	private AuditHandler auditHandler;

	@GetMapping(value = "/info")
	protected AuditAccessible.AuditMember info(
		@RequestParam("type") String type
	) throws Exception {
		AuditAccessible accessible = auditHandler.getAccessibles().values().iterator().next();
		return accessible.getMembers().get(FrameworkUtil.classOf(type));
	}
	
	@PostMapping(value = "/list")
	protected Page auditList(
		@RequestParam("handler") String handler
	) throws Exception {
		byte[] data = WebMvcUtil.getBodyAsBytes();
		AuditRequest request = auditHandler.getRequest(data);
		String entity = request.getEntity() != null ? request.getEntity().trim() : "";
		if (!entity.isEmpty()) {
			request.setClassOfEntity(FrameworkUtil.classOf(entity));
		}
		return auditHandler.getList(request);
	}
	
}
