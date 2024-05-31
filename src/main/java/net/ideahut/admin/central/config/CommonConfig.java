package net.ideahut.admin.central.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import net.ideahut.springboot.entity.EntityAnnotationIntrospector;
import net.ideahut.springboot.entity.EntityInfo;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.EntityTrxManagerImpl;
import net.ideahut.springboot.entity.TrxManagerInfo;
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
import net.ideahut.springboot.util.FrameworkUtil;

@Configuration
class CommonConfig {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private AppProperties appProperties;

	@Bean
	protected DataMapper dataMapper() {
		return new DataMapperImpl()
		.setIntrospector(new EntityAnnotationIntrospector());
	}
	
	@Bean
	protected EntityTrxManager entityTrxManager() {
		return new EntityTrxManagerImpl();
	}
	
	@Bean
	protected RedisTemplate<String, byte[]> redisTemplate() throws Exception {
		RedisProperties properties = appProperties.getRedis();
		RedisConnectionFactory connectionFactory = RedisHelper.createRedisConnectionFactory(properties, true);
		return RedisHelper.createRedisTemplate(connectionFactory, false);
	}
	
	@Bean(destroyMethod = "shutdown")
	protected TaskHandler auditTask() {
		return new TaskHandlerImpl()
		.setTaskProperties(appProperties.getTask());
    }
	
	@Bean
	protected InitHandler initHandler() {
		InitHandler.Endpoint endpoint = () -> "http://localhost:" + FrameworkUtil.getPort(applicationContext) + "/warmup";
		return new InitHandlerImpl()
		.setEndpoint(endpoint);
	}
	
	@Bean
	protected SingletonHandler singletonHandler() {
		return new SingletonHandlerImpl();
	}
	
	@Bean
	protected CrudPermission crudPermission() {
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
					allowed = FrameworkUtil.isTrue(view.getEnableRetrieve());
					break;
				case CREATE:
					allowed = FrameworkUtil.isTrue(view.getEnableCreate());
					break;
				case UPDATE:
					allowed = FrameworkUtil.isTrue(view.getEnableUpdate());
					break;
				case SAVE:
					allowed = FrameworkUtil.isTrue(view.getEnableUpdate()) || FrameworkUtil.isTrue(view.getEnableCreate());
					break;
				case DELETE, DELETES:
					allowed = FrameworkUtil.isTrue(view.getEnableDelete());
					break;
			}
			return allowed;
		};
	}
	
	@Bean
	protected CrudHandler crudHandler(
		EntityTrxManager entityTrxManager,
		DataMapper dataMapper
	) {
		
		return new CrudHandlerImpl()
		.setApplicationContext(applicationContext)
		.setEntityTrxManager(entityTrxManager)
		.setResource((manager, name) -> {
			try {
				Class<?> clazz = FrameworkUtil.classOf(name);
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
				throw FrameworkUtil.exception(e);
			}
		});
	}
	
}
