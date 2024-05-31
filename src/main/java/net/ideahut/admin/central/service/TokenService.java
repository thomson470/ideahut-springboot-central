package net.ideahut.admin.central.service;

import net.ideahut.springboot.security.SecurityUser;

public interface TokenService {

	String create(SecurityUser user);
	SecurityUser getUser(String token);
	
}
