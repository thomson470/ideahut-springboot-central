package net.ideahut.admin.central.object;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Menu {

	private String id;
	private String title;
	private String icon;
	private String link;
	private Menu parent;
	private Integer order;
	private List<Menu> children;
	
}
