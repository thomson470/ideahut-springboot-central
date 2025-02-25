package net.ideahut.admin.central.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

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
	
	public Menu setId(String id) {
		this.id = id;
		return this;
	}
	
	public Menu setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public Menu setIcon(String icon) {
		this.icon = icon;
		return this;
	}
	
	public Menu setLink(String link) {
		this.link = link;
		return this;
	}
	
	public Menu setParent(Menu parent) {
		this.parent = parent;
		return this;
	}
	
	public Menu setOrder(Integer order) {
		this.order = order;
		return this;
	}
	
	public Menu setChildren(List<Menu> children) {
		this.children = children;
		return this;
	}
	
	public Menu addChild(Menu menu) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(menu);
		return this;
	}
	
}
