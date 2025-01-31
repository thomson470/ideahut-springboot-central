package net.ideahut.admin.central.config;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.object.Access;
import net.ideahut.springboot.crud.CrudHandler;
import net.ideahut.springboot.crud.CrudHandlerImpl;
import net.ideahut.springboot.crud.CrudPermission;
import net.ideahut.springboot.crud.CrudProperties;
import net.ideahut.springboot.entity.EntityInfo;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.EntityTrxManagerImpl;
import net.ideahut.springboot.entity.TrxManagerInfo;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.init.InitHandler;
import net.ideahut.springboot.init.InitHandlerImpl;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.mapper.DataMapperImpl;
import net.ideahut.springboot.redis.RedisHelper;
import net.ideahut.springboot.redis.RedisProperties;
import net.ideahut.springboot.singleton.SingletonHandler;
import net.ideahut.springboot.singleton.SingletonHandlerImpl;
import net.ideahut.springboot.task.TaskHandler;
import net.ideahut.springboot.task.TaskHandlerImpl;

@Configuration
class CommonConfig {
	
	@Bean
	DataMapper dataMapper() {
		return new DataMapperImpl();
	}
	
	@Bean
	EntityTrxManager entityTrxManager() {
		return new EntityTrxManagerImpl();
	}
	
	@Bean
	RedisTemplate<String, byte[]> redisTemplate(
		AppProperties appProperties
	) throws Exception {
		RedisConnectionFactory connectionFactory = RedisHelper.createRedisConnectionFactory(appProperties.getRedis(), true);
		return RedisHelper.createRedisTemplate(
			connectionFactory, 
			new RedisProperties.Template()
			.setKeyType(String.class)
			.setValueType(byte[].class)
		);
	}
	
	@Bean
	TaskHandler auditTask(
		AppProperties appProperties
	) {
		return new TaskHandlerImpl()
		.setTaskProperties(appProperties.getTask());
    }
	
	@Bean
	InitHandler initHandler(
		ApplicationContext applicationContext		
	) {
		return new InitHandlerImpl()
		.setEndpoint(() -> "http://localhost:" + FrameworkHelper.getPort(applicationContext) + "/warmup");
	}
	
	@Bean
	SingletonHandler singletonHandler() {
		return new SingletonHandlerImpl();
	}
	
	@Bean
	CrudPermission crudPermission() {
		return (action, request) -> {
			Account account = Access.get().getAccount();
			if (account == null) {
				return false;
			}
			Map<Class<?>, AccountView> views = account.getViewsByClass();
			if (views == null) {
				return false;
			}
			AccountView view = views.get(request.getProperties().getEntityInfo().getEntityClass());
			if (view == null) {
				return false;
			}
			boolean allowed = false;
			switch (action) {
				case LIST, MAP, PAGE, SINGLE, UNIQUE:
					allowed = ObjectHelper.isTrue(view.getEnableRetrieve());
					break;
				case CREATE:
					allowed = ObjectHelper.isTrue(view.getEnableCreate());
					break;
				case UPDATE:
					allowed = ObjectHelper.isTrue(view.getEnableUpdate());
					break;
				case SAVE:
					allowed = ObjectHelper.isTrue(view.getEnableUpdate()) || ObjectHelper.isTrue(view.getEnableCreate());
					break;
				case DELETE, DELETES:
					allowed = ObjectHelper.isTrue(view.getEnableDelete());
					break;
			}
			return allowed;
		};
	}
	
	@Bean
	CrudHandler crudHandler(
		ApplicationContext applicationContext,
		EntityTrxManager entityTrxManager,
		DataMapper dataMapper
	) {
		
		return new CrudHandlerImpl()
		.setApplicationContext(applicationContext)
		.setEntityTrxManager(entityTrxManager)
		.setResource((manager, name) -> {
			try {
				Class<?> clazz = ObjectHelper.classOf(name);
				TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
				if (manager != null && !manager.isEmpty()) {
					trxManagerInfo = entityTrxManager.getTrxManagerInfo(manager);
				}
				EntityInfo entityInfo = trxManagerInfo.getEntityInfo(clazz);
				CrudProperties props = new CrudProperties();
				props.setEntityInfo(entityInfo);
				props.setMaxLimit(200);
				props.setUseNative(false);
				return props;
			} catch (Exception e) {
				throw ErrorHelper.exception(e);
			}
		});
	}
	
}
