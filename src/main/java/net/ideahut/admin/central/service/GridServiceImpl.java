package net.ideahut.admin.central.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Redis;
import net.ideahut.admin.central.object.View;
import net.ideahut.admin.central.support.GridSupport;
import net.ideahut.springboot.bean.BeanConfigure;
import net.ideahut.springboot.bean.BeanReload;
import net.ideahut.springboot.bean.BeanShutdown;
import net.ideahut.springboot.crud.CrudAction;
import net.ideahut.springboot.grid.GridAdditional;
import net.ideahut.springboot.grid.GridOption;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.object.Option;

@Service
class GridServiceImpl implements 
	GridService,
	BeanReload,
	BeanShutdown,
	BeanConfigure<GridService>
{
	
	private static class Strings {
		private static final String OPTIONS = "options";
		private static final String ADDITIONALS = "additionals";
	}
	
	private static class RedisKey {
		static String lock(String redisPrefix) {
			return redisPrefix + "GRID***LOCK";
		}
		static String grid(String redisPrefix, String name) {
			return redisPrefix + "GRID-" + name;
		}
	}
	
	private final AppProperties appProperties;
	private Redis redis;
	private ApplicationContext applicationContext;
	
	@Autowired
	GridServiceImpl(
		AppProperties appProperties
	) {
		this.appProperties = appProperties;
	}

	private Map<String, GridOption> gridOptions = new LinkedHashMap<>();
	private Map<String, GridAdditional> gridAdditionals = new LinkedHashMap<>();
	
	@Override
	public Callable<GridService> onConfigureBean(ApplicationContext applicationContext) {
		GridServiceImpl self = this;
		return new Callable<GridService>() {
			@Override
			public GridService call() throws Exception {
				self.applicationContext = applicationContext;
				self.redis = applicationContext.getBean(Redis.class);
				onReloadBean();
				return self;
			}
		};
	}

	@Override
	public boolean isBeanConfigured() {
		return true;
	}

	@Override
	public void onShutdownBean() {
		unlock();
	}
	
	@Override
	public boolean onReloadBean() throws Exception {
		if (!lock()) {
			return false;
		}
		try {
			clear();
			reload();
		} finally {
			unlock();
		}
		return true;
	}

	@Override
	public ObjectNode getGrid(String name) {
		DataMapper dataMapper = FrameworkHelper.defaultDataMapper();
		ValueOperations<String, byte[]> valops = redis.getTemplate().opsForValue();
		byte[] bytes = valops.get(RedisKey.grid(redis.getPrefix(), name));
		if (bytes == null) {
			return dataMapper.createObjectNode();
		}
		ObjectNode grid = redis.getSerializer().deserialize(ObjectNode.class, bytes);
		ObjectHelper.runIf(
			grid.has(Strings.OPTIONS), 
			() -> {
				JsonNode joptions = grid.get(Strings.OPTIONS);
				ObjectHelper.runOrElse(
					joptions.isArray(), 
					() -> {
						ObjectNode nodes = dataMapper.createObjectNode();
						ArrayNode jarrays = (ArrayNode) joptions;
						for (JsonNode joption : jarrays) {
							String key = joption.asText();
							GridOption gridOption = gridOptions.get(key);
							ObjectHelper.runIf(
								gridOption != null, 
								() -> {
									List<Option> option = gridOption.getOption(applicationContext);
									nodes.putArray(key).addAll(dataMapper.convert(option, ArrayNode.class));
								}
							);
						}
						grid.set(Strings.OPTIONS, nodes);
					}, 
					() -> grid.remove(Strings.OPTIONS)
				);
			}
		);
		ObjectHelper.runIf(
			grid.has(Strings.ADDITIONALS), 
			() -> {
				JsonNode jadditionals = grid.get(Strings.ADDITIONALS);
				ObjectHelper.runOrElse(
					jadditionals.isArray(), 
					() -> {
						ObjectNode nodes = dataMapper.createObjectNode();
						ArrayNode jarrays = (ArrayNode) jadditionals;
						for (JsonNode jadditional : jarrays) {
							String key = jadditional.asText();
							GridAdditional gridAdditional = gridAdditionals.get(key);
							ObjectHelper.runIf(
								gridAdditional != null, 
								() -> {
									ArrayNode node = gridAdditional.getAdditional(applicationContext);
									nodes.set(key, node);
								}
							);
						}
						grid.set(Strings.ADDITIONALS, nodes);
					}, 
					() -> grid.remove(Strings.ADDITIONALS)
				);
			}
		);
		View eview = View.of(name);
		ObjectHelper.runIf(
			eview != null, 
			() -> {
				Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
				Map<String, AccountView> views = ObjectHelper.useOrDefault(account.getViewsByName(), LinkedHashMap::new);
				AccountView view = views.get(eview.name());
				if (view != null) {
					ArrayNode actions = grid.putArray("actions");
					ObjectHelper.runIf(
						ObjectHelper.isTrue(view.getEnableRetrieve()), 
						() -> {
							actions.add(CrudAction.LIST.name());
							actions.add(CrudAction.MAP.name());
							actions.add(CrudAction.PAGE.name());
							actions.add(CrudAction.SINGLE.name());
							actions.add(CrudAction.UNIQUE.name());
						}
					);
					ObjectHelper.runIf(
						ObjectHelper.isTrue(view.getEnableCreate()), 
						() -> actions.add(CrudAction.CREATE.name())
					);
					ObjectHelper.runIf(
						ObjectHelper.isTrue(view.getEnableUpdate()), 
						() -> actions.add(CrudAction.UPDATE.name())
					);
					ObjectHelper.runIf(
						ObjectHelper.isTrue(view.getEnableDelete()), 
						() -> {
							actions.add(CrudAction.DELETE.name());
							actions.add(CrudAction.DELETES.name());
						}
					);
				}
			}
		);
		return grid;
	}
	
	private synchronized boolean lock() {
		String key = RedisKey.lock(redis.getPrefix());
		ValueOperations<String, byte[]> valops = redis.getTemplate().opsForValue();
		byte[] bytes = valops.get(key);
		if (bytes != null) {
			return false;
		}
		valops.set(key, "1".getBytes());
		return true;
	}
	
	private void unlock() {
		String key = RedisKey.lock(redis.getPrefix());
		redis.getTemplate().delete(key);
	}
	
	private void clear() {
		redis.getTemplate().delete(Arrays.asList(
			RedisKey.grid(redis.getPrefix(), View.ACCOUNT.getGrid()),
			RedisKey.grid(redis.getPrefix(), View.MODULE.getGrid()),
			RedisKey.grid(redis.getPrefix(), View.PROJECT.getGrid()),
			RedisKey.grid(redis.getPrefix(), View.REDIRECT.getGrid())
		));
		gridOptions.clear();
		gridAdditionals.clear();
	}
	
	private void reload() throws Exception {
		Map<String, byte[]> map = new HashMap<>();
		AppProperties.Grid grid = appProperties.getGrid();
		ObjectNode account = load(grid.getAccount());
		ObjectHelper.runIf(
			account != null, 
			() -> map.put
				(RedisKey.grid(redis.getPrefix(), View.ACCOUNT.getGrid()), 
				redis.getSerializer().serialize(ObjectNode.class, account)
			)
		);
		ObjectNode project = load(grid.getProject());
		ObjectHelper.runIf(
			project != null, 
			() -> map.put
				(RedisKey.grid(redis.getPrefix(), View.PROJECT.getGrid()), 
				redis.getSerializer().serialize(ObjectNode.class, project)
			)
		);
		ObjectNode module = load(grid.getModule());
		ObjectHelper.runIf(
			module != null, 
			() -> map.put
				(RedisKey.grid(redis.getPrefix(), View.MODULE.getGrid()), 
				redis.getSerializer().serialize(ObjectNode.class, module)
			)
		);
		ObjectNode redirect = load(grid.getRedirect());
		ObjectHelper.runIf(
			redirect != null, 
			() -> map.put
				(RedisKey.grid(redis.getPrefix(), View.REDIRECT.getGrid()), 
				redis.getSerializer().serialize(ObjectNode.class, redirect)
			)
		);
		gridOptions.putAll(GridSupport.getOptions());
		gridAdditionals.putAll(GridSupport.getAdditionals());
		ValueOperations<String, byte[]> valops = redis.getTemplate().opsForValue();
		valops.multiSet(map);
	}
	
	private ObjectNode load(String location) throws Exception {
		File file = new File(FrameworkHelper.replacePath(location));
		if (!file.isFile()) {
			return null;
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);
		return FrameworkHelper.loadConfiguration(bytes, ObjectNode.class);
	}
	
}
