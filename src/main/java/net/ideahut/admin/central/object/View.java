package net.ideahut.admin.central.object;

import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.entity.Project;
import net.ideahut.admin.central.entity.Redirect;

public enum View {
	ACCOUNT		(Account.class),
	PROJECT		(Project.class),
	MODULE		(Module.class),
	REDIRECT	(Redirect.class),
	;
	
	private final Class<?> type;
	
	View(Class<?> type) {
		this.type = type;
	}
	
	public Class<?> getType() {
		return type;
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
		} catch (Exception e) {}
		return null;
	}
	
}
