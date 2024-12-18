package net.ideahut.admin.central.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface GridService {

	ObjectNode getGrid(String name);
	
}
