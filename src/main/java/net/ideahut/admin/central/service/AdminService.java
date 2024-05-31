package net.ideahut.admin.central.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import net.ideahut.admin.central.entity.AccountModuleId;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.springboot.object.Page;

public interface AdminService {

	ObjectNode grid(String name);
	
	void iconSync();
	
	Page getProjects(Page page, String search, String order);
	Page getModules(String projectId, Page page, String search, String order);
	
	Module getModule(AccountModuleId id);
	
	Forward redirect(AccountModuleId id);
	
}
