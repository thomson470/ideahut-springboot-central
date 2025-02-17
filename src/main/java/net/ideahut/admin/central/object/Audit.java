package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditHandler;

@Setter
@Getter
public class Audit implements Serializable {
	private static final long serialVersionUID = -7242477820489535712L;
	
	private Map<String, AuditAccessible> accessibles;
	private transient AuditHandler handler;
	
}
