package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Forward {
	
	@Setter
	@Getter
	public static class Cookie implements Serializable {
		private static final long serialVersionUID = 4032955945627466706L;
		 
		private String name;
		private String value;
		private String domain;
		private String maxAge;
		private String path;
		private Boolean secure;
		private Boolean httpOnly;
	}
	
	private String action;
	private String method;
	private Map<String, String> parameters;
	private List<Cookie> cookies;
	
	public Forward setParameter(String name, String value) {
		if (parameters == null) {
			parameters = new LinkedHashMap<>();
		}
		parameters.put(name, value);
		return this;
	}
	
	public Forward addCookie(Cookie cookie) {
		if (cookies == null) {
			cookies = new ArrayList<>();
		}
		cookies.add(cookie);
		return this;
	}
}
