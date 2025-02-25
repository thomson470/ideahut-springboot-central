package net.ideahut.admin.central.object;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class Cookie implements Serializable {
	private static final long serialVersionUID = -9089150896289596981L;

	private String name;
	private String value;
	private String domain;
	private String maxAge;
	private String path;
	private Boolean secure;
	private Boolean httpOnly;
	
	public Cookie setName(String name) {
		this.name = name;
		return this;
	}
	
	public Cookie setValue(String value) {
		this.value = value;
		return this;
	}
	
	public Cookie setDomain(String domain) {
		this.domain = domain;
		return this;
	}
	
	public Cookie setMaxAge(String maxAge) {
		this.maxAge = maxAge;
		return this;
	}
	
	public Cookie setPath(String path) {
		this.path = path;
		return this;
	}
	
	public Cookie setSecure(Boolean secure) {
		this.secure = secure;
		return this;
	}
	
	public Cookie setHttpOnly(Boolean httpOnly) {
		this.httpOnly = httpOnly;
		return this;
	}
	
}
