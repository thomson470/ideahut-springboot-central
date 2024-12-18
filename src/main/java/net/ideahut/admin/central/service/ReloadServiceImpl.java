package net.ideahut.admin.central.service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.ideahut.springboot.bean.BeanConfigure;
import net.ideahut.springboot.bean.BeanReload;
import net.ideahut.springboot.helper.ErrorHelper;

@Service
public class ReloadServiceImpl implements ReloadService, BeanConfigure<ReloadService> {
	
	private boolean configured = false;
	private Map<String, BeanReload> beans = new LinkedHashMap<>();

	@Override
	public Callable<ReloadService> onConfigureBean(ApplicationContext applicationContext) {
		ReloadServiceImpl self = this;
		return new Callable<ReloadService>() {
			@Override
			public ReloadService call() throws Exception {
				beans.clear();
				beans.putAll(applicationContext.getBeansOfType(BeanReload.class));
				self.configured = true;
				return self;
			}
		};
	}

	@Override
	public boolean isBeanConfigured() {
		return configured;
	}
	
	@Override
	public Set<String> names() {
		BeanConfigure.checkBeanConfigure(this);
		return beans.keySet();
	}

	@Override
	public boolean reload(String name) throws Exception {
		BeanConfigure.checkBeanConfigure(this);
		BeanReload bean = beans.get(name);
		ErrorHelper.throwIf(bean == null, "Bean not found, for name: {}", name);
		return bean.onReloadBean();
	}

	

}
