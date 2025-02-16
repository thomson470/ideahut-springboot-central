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
import net.ideahut.admin.central.object.Redis;
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
import net.ideahut.springboot.serializer.BinarySerializer;
import net.ideahut.springboot.serializer.DataMapperBinarySerializer;
import net.ideahut.springboot.serializer.FuryBinarySerializer;
import net.ideahut.springboot.serializer.HessianBinarySerializer;
import net.ideahut.springboot.serializer.JdkBinarySerializer;
import net.ideahut.springboot.serializer.KryoBinarySerializer;
import net.ideahut.springboot.singleton.SingletonHandler;
import net.ideahut.springboot.singleton.SingletonHandlerImpl;
import net.ideahut.springboot.task.TaskHandler;
import net.ideahut.springboot.task.TaskHandlerImpl;

@Configuration
class CommonConfig {
	
	@Bean
	InitHandler initHandler(
		ApplicationContext applicationContext		
	) {
		return new InitHandlerImpl()
		.setEndpoint(() -> "http://localhost:" + FrameworkHelper.getPort(applicationContext) + "/warmup");
	}
	
	@Bean
	DataMapper dataMapper() {
		return new DataMapperImpl();
	}
	
	@Bean
	BinarySerializer binarySerializer(
		AppProperties appProperties,
		DataMapper dataMapper
	) {
		AppProperties.Binary binary = ObjectHelper.useOrDefault(appProperties.getBinary(), AppProperties.Binary::new);
		String code = ObjectHelper.useOrDefault(appProperties.getBinarySerializer(), "").trim().toLowerCase();
		if ("xml".equals(code)) {
			return new DataMapperBinarySerializer().setMapper(dataMapper).setFormat(DataMapper.XML);
		}
		else if ("jdk".equals(code)) {
			return new JdkBinarySerializer();
		}
		else if ("fury".equals(code)) {
			return new FuryBinarySerializer(binary.getFury());
		}
		else if ("kryo".equals(code)) {
			return new KryoBinarySerializer(binary.getKryo());
		}
		else if ("hessian1".equals(code)) {
			return new HessianBinarySerializer().setVersion(1);
		}
		else if ("hessian2".equals(code)) {
			return new HessianBinarySerializer().setVersion(2);
		}
		else {
			return new DataMapperBinarySerializer().setMapper(dataMapper).setFormat(DataMapper.JSON);
		}
	}
	
	@Bean
	EntityTrxManager entityTrxManager(
		AppProperties appProperties
	) {
		return new EntityTrxManagerImpl()
		.setForeignKeyParam(appProperties.getForeignKey());
	}
	
	@Bean
	Redis redis(
		AppProperties appProperties,
		ApplicationContext applicationContext,
		BinarySerializer binarySerializer
	) throws Exception {
		AppProperties.Redis redis = ObjectHelper.useOrDefault(appProperties.getRedis(), AppProperties.Redis::new);
		RedisConnectionFactory connectionFactory = RedisHelper.createRedisConnectionFactory(redis.getConnection(), true);
		RedisTemplate<String, byte[]> template = RedisHelper.createRedisTemplate(
			connectionFactory, 
			new RedisProperties.Template()
			.setKeyType(String.class)
			.setValueType(byte[].class)
		);
		template.afterPropertiesSet();
		return Redis.of(template, redis.getStorageKeyParam(), applicationContext, binarySerializer);
	}
	
	@Bean
	TaskHandler taskHandler(
		AppProperties appProperties
	) {
		return new TaskHandlerImpl()
		.setTaskProperties(appProperties.getTask());
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
