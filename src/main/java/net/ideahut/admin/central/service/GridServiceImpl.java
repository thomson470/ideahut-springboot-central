package net.ideahut.admin.central.service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.View;
import net.ideahut.admin.central.support.GridSupport;
import net.ideahut.springboot.bean.BeanReload;
import net.ideahut.springboot.crud.CrudAction;
import net.ideahut.springboot.grid.GridAdditional;
import net.ideahut.springboot.grid.GridOption;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.object.Option;

@Service
class GridServiceImpl implements GridService, InitializingBean, BeanReload {
	
	private static class Strings {
		private static final String OPTIONS = "options";
		private static final String ADDITIONALS = "additionals";
		
		private static final String ACCOUNT = "account";
		private static final String PROJECT = "project";
		private static final String MODULE = "module";
		private static final String REDIRECT = "redirect";
	}
	
	private final AppProperties appProperties;
	private final ApplicationContext applicationContext;
	private final DataMapper dataMapper;
	
	@Autowired
	GridServiceImpl(
		AppProperties appProperties,
		ApplicationContext applicationContext,
		DataMapper dataMapper
	) {
		this.appProperties = appProperties;
		this.applicationContext = applicationContext;
		this.dataMapper = dataMapper;
	}

	private Map<String, ObjectNode> gridTemplates = new LinkedHashMap<>();
	private Map<String, GridOption> gridOptions = new LinkedHashMap<>();
	private Map<String, GridAdditional> gridAdditionals = new LinkedHashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		onReloadBean();
	}
	
	@Override
	public boolean onReloadBean() throws Exception {
		gridTemplates.clear();
		gridOptions.clear();
		gridAdditionals.clear();
		AppProperties.Grid grid = appProperties.getGrid();
		ObjectNode account = load(grid.getAccount());
		ObjectHelper.runIf(
			account != null, 
			() -> gridTemplates.put(Strings.ACCOUNT, account)
		);
		ObjectNode project = load(grid.getProject());
		ObjectHelper.runIf(
			project != null, 
			() -> gridTemplates.put(Strings.PROJECT, project)
		);
		ObjectNode module = load(grid.getModule());
		ObjectHelper.runIf(
			module != null, 
			() -> gridTemplates.put(Strings.MODULE, module)
		);
		ObjectNode redirect = load(grid.getRedirect());
		ObjectHelper.runIf(
			redirect != null, 
			() -> gridTemplates.put(Strings.REDIRECT, redirect)
		);
		gridOptions.putAll(GridSupport.getOptions());
		gridAdditionals.putAll(GridSupport.getAdditionals());
		return true;
	}

	@Override
	public ObjectNode getGrid(String name) {
		ObjectNode template = gridTemplates.get(name);
		if (template == null) {
			return dataMapper.createObjectNode();
		}
		ObjectNode grid = dataMapper.copy(template, ObjectNode.class);
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
	
	private ObjectNode load(String location) throws Exception {
		File file = new File(FrameworkHelper.replacePath(location));
		if (!file.isFile()) {
			return null;
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);
		return FrameworkHelper.loadConfiguration(bytes, ObjectNode.class);
	}
	
}
