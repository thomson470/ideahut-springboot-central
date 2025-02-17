package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Menu implements Serializable {
	private static final long serialVersionUID = 604883850000987657L;
	
	private String id;
	private String title;
	private String icon;
	private String link;
	private Menu parent;
	private Integer order;
	private List<Menu> children;
	
}
