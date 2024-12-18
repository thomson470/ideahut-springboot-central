package net.ideahut.admin.central.service;

import java.util.Set;

public interface ReloadService {

	Set<String> names();
	boolean reload(String name) throws Exception;
	
}
