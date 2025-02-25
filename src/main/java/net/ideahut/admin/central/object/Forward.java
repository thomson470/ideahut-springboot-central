package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.ideahut.springboot.object.StringMap;

@Getter
public class Forward implements Serializable {
	private static final long serialVersionUID = -5344648877204941644L;
	
	private String action;
	private String method;
	private StringMap parameters;
	private List<Cookie> cookies;
	
	public Forward setAction(String action) {
		this.action = action;
		return this;
	}

	public Forward setMethod(String method) {
		this.method = method;
		return this;
	}

	public Forward setParameters(StringMap parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public Forward putParameter(String name, String value) {
		if (name != null) {
			if (parameters == null) {
				parameters = new StringMap();
			}
			parameters.put(name, value);
		}
		return this;
	}

	public Forward setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
		return this;
	}
	
	public Forward addCookie(Cookie cookie) {
		if (cookie != null) {
			if (cookies == null) {
				cookies = new ArrayList<>();
			}
			cookies.add(cookie);
		}
		return this;
	}
	
}
