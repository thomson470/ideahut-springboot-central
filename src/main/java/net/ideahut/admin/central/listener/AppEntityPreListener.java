package net.ideahut.admin.central.listener;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan
class AppEntityPreListener implements net.ideahut.springboot.entity.EntityPreListener {
	
	@Override
	public void onPreDelete(Object entity) {
		/**/
	}

	@Override
	public void onPreUpdate(Object entity) {
		/**/
	}

	@Override
	public void onPreInsert(Object entity) {
		/**/
	}
	
}
