package net.ideahut.admin.central.object;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditHandler;

@Setter
@Getter
public class Audit {

	private Map<String, AuditAccessible> accessibles;
	private AuditHandler handler;
	
}
