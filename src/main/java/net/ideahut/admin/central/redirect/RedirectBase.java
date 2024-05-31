package net.ideahut.admin.central.redirect;

import org.springframework.context.ApplicationContext;

import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.object.Forward;

public abstract class RedirectBase {
	
	private final ApplicationContext applicationContext;
	
	protected RedirectBase(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public abstract Forward forward(Module module);
	
}
