package net.ideahut.admin.central.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import net.ideahut.springboot.audit.AuditHandler;

@Component
@ComponentScan
class AppEntityPostListener implements net.ideahut.springboot.entity.EntityPostListener {
	
	private final AuditHandler auditHandler;
	
	@Autowired
	AppEntityPostListener(
		AuditHandler auditHandler
	) {
		this.auditHandler = auditHandler;
	}

	@Override
	public void onPostDelete(Object entity) {
		auditHandler.save("DELETE", entity);
	}

	@Override
	public void onPostInsert(Object entity) {
		auditHandler.save("INSERT", entity);
	}

	@Override
	public void onPostUpdate(Object entity) {
		auditHandler.save("UPDATE", entity);
	}
	
}
