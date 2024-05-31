package net.ideahut.admin.central.service;

import net.ideahut.admin.central.entity.Account;

public interface AccountService {

	Account getAccountByUsername(String username);
	
}
