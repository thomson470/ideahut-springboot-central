package net.ideahut.admin.central.service;

import net.ideahut.admin.central.entity.AccountModuleId;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.springboot.object.Page;

public interface AdminService {

	String getAdminVersion();
	byte[] getAdminBytes();
	String getAdminRedirectUrl();
	boolean isAdminRedirect();
	void saveAdmin(byte[] bytes);
	
	void syncImages();
	
	Page getProjects(Page page, String search, String order);
	Page getModules(String projectId, Page page, String search, String order);
	
	Module getModule(AccountModuleId id);
	Forward getForward(AccountModuleId id);
	
}
