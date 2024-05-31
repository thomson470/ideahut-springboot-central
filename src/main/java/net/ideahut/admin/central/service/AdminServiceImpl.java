package net.ideahut.admin.central.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountModuleId;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.entity.ModuleConfiguration;
import net.ideahut.admin.central.entity.Project;
import net.ideahut.admin.central.entity.ProjectModule;
import net.ideahut.admin.central.entity.Redirect;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.admin.central.object.View;
import net.ideahut.admin.central.redirect.RedirectBase;
import net.ideahut.admin.central.support.GridSupport;
import net.ideahut.springboot.crud.CrudAction;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.SessionCallable;
import net.ideahut.springboot.entity.TrxManagerInfo;
import net.ideahut.springboot.grid.GridAdditional;
import net.ideahut.springboot.grid.GridOption;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.object.Option;
import net.ideahut.springboot.object.Page;
import net.ideahut.springboot.singleton.SingletonHandler;
import net.ideahut.springboot.task.TaskHandler;
import net.ideahut.springboot.util.FrameworkUtil;

@Service
public class AdminServiceImpl implements AdminService, InitializingBean {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private SingletonHandler singletonHandler;
	@Autowired
	private DataMapper dataMapper;
	@Autowired
	private EntityTrxManager entityTrxManager;
	@Autowired
	private TaskHandler taskHandler;

	private Map<String, ObjectNode> gridTemplates = new LinkedHashMap<>();
	private Map<String, GridOption> gridOptions = new LinkedHashMap<>();
	private Map<String, GridAdditional> gridAdditionals = new LinkedHashMap<>();
	
	@Override
	public void afterPropertiesSet() throws Exception {
		AppProperties.Grid grid = appProperties.getGrid();
		ObjectNode account = load(grid.getAccount());
		if (account != null) {
			gridTemplates.put("account", account);
		}
		ObjectNode project = load(grid.getProject());
		if (project != null) {
			gridTemplates.put("project", project);
		}
		ObjectNode module = load(grid.getModule());
		if (module != null) {
			gridTemplates.put("module", module);
		}
		ObjectNode redirect = load(grid.getRedirect());
		if (redirect != null) {
			gridTemplates.put("redirect", redirect);
		}
		gridOptions.putAll(GridSupport.getOptions());
		gridAdditionals.putAll(GridSupport.getAdditionals());
	}
	
	private ObjectNode load(String location) throws Exception {
		File file = new File(FrameworkUtil.replacePath(location));
		if (!file.isFile()) {
			return null;
		}
		byte[] bytes = FileUtils.readFileToByteArray(file);
		return dataMapper.read(bytes, ObjectNode.class);
	}
	
	@Override
	public ObjectNode grid(String name) {
		ObjectNode grid = null;
		ObjectNode template = gridTemplates.get(name);
		if (template != null) {
			grid = dataMapper.copy(template, ObjectNode.class);
			if (grid.has("options")) {
				JsonNode joptions = grid.get("options");
				if(joptions.isArray()) {
					ObjectNode nodes = dataMapper.createObjectNode();
					ArrayNode jarrays = (ArrayNode) joptions;
					for (JsonNode joption : jarrays) {
						String key = joption.asText();
						GridOption gridOption = gridOptions.get(key);
						if (gridOption != null) {
							List<Option> option = gridOption.getOption(applicationContext);
							nodes.putArray(key).addAll(dataMapper.convert(option, ArrayNode.class));
						}
					}
					grid.set("options", nodes);
				} else {
					grid.remove("options");
				}
			}
			if (grid.has("additionals")) {
				JsonNode jadditionals = grid.get("additionals");
				if(jadditionals.isArray()) {
					ObjectNode nodes = dataMapper.createObjectNode();
					ArrayNode jarrays = (ArrayNode) jadditionals;
					for (JsonNode jadditional : jarrays) {
						String key = jadditional.asText();
						GridAdditional gridAdditional = gridAdditionals.get(key);
						if (gridAdditional != null) {
							ArrayNode node = gridAdditional.getAdditional(applicationContext);
							nodes.set(key, node);
						}
					}
					grid.set("additionals", nodes);
				} else {
					grid.remove("additionals");
				}
			}
			View eview = View.of(name);
			if (eview != null) {
				Account account = FrameworkUtil.getOrDefault(Access.get().getAccount(), new Account());
				Map<String, AccountView> views = FrameworkUtil.getOrDefault(account.getViewsByName(), new LinkedHashMap<>());
				AccountView view = views.get(eview.name());
				if (view != null) {
					ArrayNode actions = grid.putArray("actions");
					if (FrameworkUtil.isTrue(view.getEnableRetrieve())) {
						actions.add(CrudAction.LIST.name());
						actions.add(CrudAction.MAP.name());
						actions.add(CrudAction.PAGE.name());
						actions.add(CrudAction.SINGLE.name());
						actions.add(CrudAction.UNIQUE.name());
					}
					if (FrameworkUtil.isTrue(view.getEnableCreate())) {
						actions.add(CrudAction.CREATE.name());
					}
					if (FrameworkUtil.isTrue(view.getEnableUpdate())) {
						actions.add(CrudAction.UPDATE.name());
					}
					if (FrameworkUtil.isTrue(view.getEnableDelete())) {
						actions.add(CrudAction.DELETE.name());
						actions.add(CrudAction.DELETES.name());
					}
				}
			}
		}
		return grid;
	}

	@Override
	public void iconSync() {
		taskHandler.execute(() -> delIcons("project"));
		taskHandler.execute(() -> delIcons("module"));
	}
	
	private void delIcons(String type) {
		List<String> icons = getIcons(type);
		File directory = appProperties.getMultimedia().directory(type);
		if (!directory.isDirectory()) {
			return;
		}
		List<File> files = Arrays.asList(directory.listFiles()).stream().filter(f -> !icons.contains(f.getName())).toList();
		for (File file : files) {
			if (file.isFile()) {
				FileUtils.deleteQuietly(file);
			}
		}
	}
	
	private List<String> getIcons(String type) {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		return trxManagerInfo.transaction(new SessionCallable<List<String>>() {
			@Override
			public List<String> call(Session session) throws Exception {
				List<String> items = session.createQuery(
					"select icon from " + (type.substring(0, 1).toUpperCase() + type.substring(1)) + " where icon is not null and icon != ''", 
					String.class
				).getResultList();
				List<String> icons = new ArrayList<>();
				String prefix = "/" + type + "/";
				while (!items.isEmpty()) {
					String item = items.remove(0);
					if (item.startsWith(prefix)) {
						icons.add(item.substring(prefix.length()));
					}
				}
				return icons;
			}
		});
	}

	@Override
	public Page getProjects(Page inPage, String search, String order) {
		Page page = Page.of(inPage.getIndex(), inPage.getSize(), inPage.getCount());
		Account account = FrameworkUtil.getOrDefault(Access.get().getAccount(), new Account());
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		return trxManagerInfo.transaction(new SessionCallable<Page>() {
			@Override
			public Page call(Session session) throws Exception {
				List<Object> parameters = new ArrayList<>();
				String hql = 
					"select a.project, count(a.id.moduleId) " +
					"from AccountModule a " +
					"where a.id.accountId=?1 ";
				parameters.add(account.getAccountId());
				if (search != null && !search.trim().isEmpty()) {
					hql += "and (lower(a.project.name) like ?2 or lower(a.project.description) like ?2) ";
					parameters.add("%"+ search.toLowerCase() + "%");
				}
				hql += "group by a.project ";
				String lorder = order != null ? order.trim() : "";
				if (!lorder.isEmpty()) {
					boolean desc = lorder.startsWith("-");
					if (desc) {
						lorder = lorder.substring(1);
					}
					hql += "order by a.project." + lorder + " " + (desc ? "desc" : "asc") + " ";
				}
				Query<Object[]> query = session.createQuery(hql, Object[].class);
				for (int i = 0; i < parameters.size(); i++) {
					query.setParameter(i + 1, parameters.get(i));
				}
				query.setFirstResult((page.getIndex() - 1) * page.getSize());
				query.setMaxResults(page.getSize());
				List<Object[]> items = query.getResultList();
				List<Project> projects = new ArrayList<>();
				while (!items.isEmpty()) {
					Object[] item = items.remove(0);
					Project project = (Project) item[0];
					trxManagerInfo.loadLazy(project, Project.class);
					Integer modules = Integer.parseInt(item[1] + "");
					project.setModules(modules);
					projects.add(project);
				}
				page.setCount(false);
				page.setData(projects);
				return page;
			}
		});
	}

	@Override
	public Page getModules(String projectId, Page inPage, String search, String order) {
		String fProjectId = projectId != null ? projectId.trim() : "";
		FrameworkUtil.throwIf(fProjectId.isEmpty(), "projectId is required");
		Page page = Page.of(inPage.getIndex(), inPage.getSize(), inPage.getCount());
		Account account = FrameworkUtil.getOrDefault(Access.get().getAccount(), new Account());
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		return trxManagerInfo.transaction(new SessionCallable<Page>() {
			@Override
			public Page call(Session session) throws Exception {
				List<Object> parameters = new ArrayList<>();
				String hql = 
					"select a.isActive, b, c " +
					"from AccountModule a " +
					"join ProjectModule b on b.id.projectId=a.id.projectId and b.id.moduleId=a.id.moduleId " +
					"join Module c on c.moduleId=a.id.moduleId " +
					"where a.id.accountId=?1 " +
					"and a.id.projectId=?2 ";
				parameters.add(account.getAccountId());
				parameters.add(fProjectId);
				if (search != null && !search.trim().isEmpty()) {
					hql += "and (lower(b.name) like ?3 or lower(b.description) like ?3 or lower(c.name) like ?3 or lower(c.description) like ?3) ";
					parameters.add("%"+ search.toLowerCase() + "%");
				}
				String lorder = order != null ? order.trim() : "";
				if (!lorder.isEmpty()) {
					boolean desc = lorder.startsWith("-");
					if (desc) {
						lorder = lorder.substring(1);
					}
					hql += "order by c." + lorder + " " + (desc ? "desc" : "asc") + " ";
				}
				Query<Object[]> query = session.createQuery(hql, Object[].class);
				for (int i = 0; i < parameters.size(); i++) {
					query.setParameter(i + 1, parameters.get(i));
				}
				query.setFirstResult((page.getIndex() - 1) * page.getSize());
				query.setMaxResults(page.getSize());
				List<Object[]> items = query.getResultList();
				List<Module> modules = new ArrayList<>();
				while (!items.isEmpty()) {
					Object[] item = items.remove(0);
					Character isActive = (Character) item[0];
					ProjectModule projectModule = (ProjectModule) item[1];
					trxManagerInfo.loadLazy(projectModule, ProjectModule.class);
					Module module = (Module) item[2];
					trxManagerInfo.loadLazy(module, Module.class);
					if (FrameworkUtil.isTrue(module.getIsActive())) {
						if (FrameworkUtil.isTrue(projectModule.getIsActive())) {
							module.setIsActive(isActive);
						} else {
							module.setIsActive(projectModule.getIsActive());
						}
					}
					if (projectModule.getName() != null && !projectModule.getName().isEmpty()) {
						module.setName(projectModule.getName());
					}
					if (projectModule.getDescription() != null && !projectModule.getDescription().isEmpty()) {
						module.setDescription(projectModule.getDescription());
					}
					modules.add(module);
				}
				page.setCount(false);
				page.setData(modules);
				return page;
			}
		});
	}

	@Override
	public Module getModule(AccountModuleId id) {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		Future<Module> ftModule = taskHandler.submit(new Callable<Module>() {
			@Override
			public Module call() throws Exception {
				return trxManagerInfo.transaction(new SessionCallable<Module>() {
					@Override
					public Module call(Session session) throws Exception {
						Object[] item = session.createQuery(
							"select a.isActive, b, c, d, e " +
							"from AccountModule a " +
							"join ProjectModule b on b.id.projectId=a.id.projectId and b.id.moduleId=a.id.moduleId " +
							"join Project c on c.projectId=a.id.projectId " +
							"join Module d on d.moduleId=a.id.moduleId " +
							"join Redirect e on e.redirectId=d.redirect.redirectId " +
							"where a.id=?1 ", 
							Object[].class
						)
						.setParameter(1, id)
						.uniqueResult();
						if (item == null) {
							return null;
						}
						Character isActive = (Character) item[0];
						ProjectModule projectModule = (ProjectModule) item[1];
						trxManagerInfo.loadLazy(projectModule, ProjectModule.class).nullAudit(projectModule);
						Project project = (Project) item[2];
						trxManagerInfo.loadLazy(project, Project.class).nullAudit(project);
						Module module = (Module) item[3];
						trxManagerInfo.loadLazy(module, Module.class).nullAudit(module);
						Redirect redirect = (Redirect) item[4];
						trxManagerInfo.loadLazy(redirect, Redirect.class).nullAudit(redirect);
						
						module.setProject(project);
						module.setRedirect(redirect);
						if (FrameworkUtil.isTrue(module.getIsActive())) {
							if (FrameworkUtil.isTrue(projectModule.getIsActive())) {
								module.setIsActive(isActive);
							} else {
								module.setIsActive(projectModule.getIsActive());
							}
						}
						if (projectModule.getName() != null && !projectModule.getName().isEmpty()) {
							module.setName(projectModule.getName());
						}
						if (projectModule.getDescription() != null && !projectModule.getDescription().isEmpty()) {
							module.setDescription(projectModule.getDescription());
						}
						return module;
					}
				});
			}
		});
		Future<Map<String, String>> ftConfig = taskHandler.submit(new Callable<Map<String, String>>() {
			@Override
			public Map<String, String> call() throws Exception {
				List<ModuleConfiguration> items = trxManagerInfo.transaction(new SessionCallable<List<ModuleConfiguration>>() {
					@Override
					public List<ModuleConfiguration> call(Session session) throws Exception {
						List<ModuleConfiguration> list = session.createQuery(
							"select a " +
							"from ModuleConfiguration a " + 
							"inner join AccountModule b on b.id.moduleId = a.id.moduleId " +
							"where b.id = ?1 ", 
							ModuleConfiguration.class
						)
						.setParameter(1, id)
						.getResultList();
						trxManagerInfo.loadLazy(list, ModuleConfiguration.class);
						return list;
					}
				});
				Map<String, String> map = new LinkedHashMap<>();
				while (!items.isEmpty()) {
					ModuleConfiguration item = items.remove(0);
					map.put(item.getId().getParameterName(), item.getValue());
				}
				return map;
			}
		});
		
		Module module = null;
		try {
			module = ftModule.get(30, TimeUnit.SECONDS);
			Map<String, String> configurations = ftConfig.get(30, TimeUnit.SECONDS);
			if (module != null) {
				module.setConfigurations(configurations);
			}
		} catch (Exception e) {
			throw FrameworkUtil.exception(e);
		}
		return module;
	}

	@Override
	public Forward redirect(AccountModuleId id) {
		Module module = getModule(id);
		FrameworkUtil.throwIf(module == null, "Module is not found");
		FrameworkUtil.throwIf(!FrameworkUtil.isTrue(module.getIsActive()), "Module is not active");
		FrameworkUtil.throwIf(!FrameworkUtil.isTrue(module.getProject().getIsActive()), "Project is not active");
		Redirect redirect = module.getRedirect();
		RedirectBase processor = singletonHandler.getSingleton(
			"REDIRECT-" + redirect.getClassname(), 
			new Callable<RedirectBase>() {
				@Override
				public RedirectBase call() throws Exception {
					return FrameworkUtil.newInstance(redirect.getClassname(), new Class<?>[] { ApplicationContext.class },  applicationContext);
				}
			}, 
			applicationContext.getId()
		);
		return processor.forward(module);
	}
	
}
