package net.ideahut.admin.central.service;

import java.util.List;

import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Menu;

public interface AccessService {

	Access login(String username, String password);
	
	Access logout(String authorization);
	
	Access info(String authorization);
	
	List<Menu> menus();
	
}
