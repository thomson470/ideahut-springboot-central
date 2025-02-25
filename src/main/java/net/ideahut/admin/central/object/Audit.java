package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditHandler;

@Getter
public class Audit implements Serializable {
	private static final long serialVersionUID = -7242477820489535712L;
	
	private Map<String, AuditAccessible> accessibles;
	private transient AuditHandler handler;
	
	public Audit setAccessibles(Map<String, AuditAccessible> accessibles) {
		this.accessibles = accessibles;
		return this;
	}
	
	public Audit putAccessible(String name, AuditAccessible accessible) {
		if (name != null) {
			if (accessibles == null) {
				accessibles = new LinkedHashMap<>();
			}
			accessibles.put(name, accessible);
		}
		return this;
	}
	
	public Audit setHandler(AuditHandler handler) {
		this.handler = handler;
		return this;
	}
	
}
