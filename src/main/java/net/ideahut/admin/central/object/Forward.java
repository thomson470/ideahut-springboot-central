package net.ideahut.admin.central.object;

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
	public static class Cookie {
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
