package net.ideahut.admin.central.object;

import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.entity.Project;
import net.ideahut.admin.central.entity.Redirect;

public enum View {
	ACCOUNT		(Account.class, "account"),
	PROJECT		(Project.class, "project"),
	MODULE		(Module.class, "module"),
	REDIRECT	(Redirect.class, "redirect"),
	;
	
	private final Class<?> type;
	private final String grid;
	
	View(Class<?> type, String grid) {
		this.type = type;
		this.grid = grid;
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public String getGrid() {
		return grid;
	}

	public static View of(Class<?> type) {
		for (View view : View.values()) {
			if (view.type.equals(type)) {
				return view;
			}
		}
		return null;
	}
	
	public static View of(String name) {
		try {
			return View.valueOf(name.trim().toUpperCase());
		} catch (Exception e) { /***/}
		return null;
	}
	
}
