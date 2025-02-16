package net.ideahut.admin.central.service;

import java.io.File;
import java.nio.file.Path;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountModuleId;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.entity.ModuleConfiguration;
import net.ideahut.admin.central.entity.Project;
import net.ideahut.admin.central.entity.ProjectModule;
import net.ideahut.admin.central.entity.Redirect;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.admin.central.object.Redis;
import net.ideahut.admin.central.redirect.RedirectBase;
import net.ideahut.springboot.bean.BeanConfigure;
import net.ideahut.springboot.bean.BeanReload;
import net.ideahut.springboot.bean.BeanShutdown;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.SessionCallable;
import net.ideahut.springboot.entity.TrxManagerInfo;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.object.Page;
import net.ideahut.springboot.object.TimeValue;
import net.ideahut.springboot.singleton.SingletonHandler;
import net.ideahut.springboot.task.TaskHandler;

@Service
class AdminServiceImpl implements 
	AdminService,
	BeanReload, 
	BeanShutdown,
	BeanConfigure<AdminService>
{
	
	final AppProperties appProperties;
	private final TaskHandler taskHandler;
	
	private TrxManagerInfo trxManagerInfo;
	private SingletonHandler singletonHandler;
	Redis redis;
	ApplicationContext applicationContext;
	
	Path adminFile;
	private boolean isAdminRedirect;
	private String adminRedirectUrl;
	
	@Autowired
	AdminServiceImpl(
		AppProperties appProperties,
		TaskHandler taskHandler
	) {
		this.appProperties = appProperties;
		this.taskHandler = taskHandler;
	}
	
	@Override
	public Callable<AdminService> onConfigureBean(ApplicationContext applicationContext) {
		AdminServiceImpl self = this;
		return new Callable<AdminService>() {
			@Override
			public AdminService call() throws Exception {
				self.applicationContext = applicationContext;
				self.singletonHandler = applicationContext.getBean(SingletonHandler.class);
				self.trxManagerInfo = applicationContext.getBean(EntityTrxManager.class).getDefaultTrxManagerInfo();
				self.redis = applicationContext.getBean(Redis.class);
				AppProperties.Multimedia multimedia = ObjectHelper.useOrDefault(appProperties.getMultimedia(), AppProperties.Multimedia::new);
				AppProperties.AdminFile fileAdmin = ObjectHelper.useOrDefault(appProperties.getAdminFile(), AppProperties.AdminFile::new);
				self.adminFile = Path.of(FrameworkHelper.replacePath(multimedia.getLocation().trim())  + "/" + fileAdmin.getName());
				self.isAdminRedirect = Boolean.TRUE.equals(fileAdmin.getRedirect());
				self.adminRedirectUrl = multimedia.getUrl() + "/" + fileAdmin.getName();
				AdminHelper.prepareUI(self);
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
	public boolean onReloadBean() throws Exception {
		if (!AdminHelper.lock(this)) {
			return false;
		}
		try {
			AdminHelper.clear(this);
			AdminHelper.reload(this);
		} finally {
			AdminHelper.unlock(this);
		}
		return true;
	}
	
	@Override
	public void onShutdownBean() {
		AdminHelper.unlock(this);
	}
	
	
	/*
	 * ADMIN WEB
	 */
	@Override
	public String getAdminVersion() {
		return AdminHelper.version(this);
	}
	@Override
	public byte[] getAdminBytes() {
		return AdminHelper.bytes(this);
	}
	@Override
	public boolean isAdminRedirect() {
		return isAdminRedirect;
	}
	@Override
	public String getAdminRedirectUrl() {
		return adminRedirectUrl;
	}
	@Override
	public void saveAdmin(byte[] bytes) {
		AdminHelper.save(this, bytes);
	}
	
	
	/*
	 * SYNC IMAGES
	 */
	@Override
	public void syncImages() {
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

	
	/*
	 * PROJECT
	 */
	@Override
	public Page getProjects(Page inPage, String search, String order) {
		Page page = Page.of(inPage);
		Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
		return trxManagerInfo.transaction(new SessionCallable<Page>() {
			@Override
			public Page call(Session session) throws Exception {
				List<Object> parameters = new ArrayList<>();
				StringBuilder hql = new StringBuilder()
				.append("select project, count(id.moduleId) ")
				.append("from AccountModule ")
				.append("where id.accountId=?1 ");
				parameters.add(account.getAccountId());
				if (search != null && !search.trim().isEmpty()) {
					hql .append("and (lower(project.name) like ?2 or lower(project.description) like ?2) ");
					parameters.add("%"+ search.toLowerCase() + "%");
				}
				hql.append("group by project ");
				String lorder = ObjectHelper.useOrDefault(order, "").trim();
				ObjectHelper.runIf(
					!lorder.isEmpty(), 
					() -> {
						boolean desc = lorder.startsWith("-");
						String field = ObjectHelper.callOrElse(desc, () -> lorder.substring(1), () -> lorder);
						hql
						.append("order by project.")
						.append(field)
						.append(desc ? " desc " : " asc ");
					}
				);
				Query<Object[]> query = session.createQuery(hql.toString(), Object[].class);
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
	
	
	/*
	 * MODULE
	 */
	private void updateModule(Module module, ProjectModule projectModule, Character isActive) {
		if (ObjectHelper.isTrue(module.getIsActive())) {
			if (ObjectHelper.isTrue(projectModule.getIsActive())) {
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
	}
	@Override
	public Page getModules(String projectId, Page inPage, String search, String order) {
		String fProjectId = ObjectHelper.useOrDefault(projectId, "").trim();
		Assert.hasLength(fProjectId, "projectId required");
		Page page = Page.of(inPage);
		Account account = ObjectHelper.useOrDefault(Access.get().getAccount(), Account::new);
		Assert.hasLength(account.getAccountId(), "accountId required");
		return trxManagerInfo.transaction(new SessionCallable<Page>() {
			@Override
			public Page call(Session session) throws Exception {
				List<Object> parameters = new ArrayList<>();
				StringBuilder hql = new StringBuilder()
				.append("select a.isActive, b, c ")
				.append("from AccountModule a ")
				.append("join ProjectModule b on b.id.projectId=a.id.projectId and b.id.moduleId=a.id.moduleId ")
				.append("join Module c on c.moduleId=a.id.moduleId ")
				.append("where a.id.accountId=?1 ")
				.append("and a.id.projectId=?2 ");
				parameters.add(account.getAccountId());
				parameters.add(fProjectId);
				ObjectHelper.runIf(
					search != null && !search.trim().isEmpty(), 
					() -> {
						hql.append("and (lower(b.name) like ?3 or lower(b.description) like ?3 or lower(c.name) like ?3 or lower(c.description) like ?3) ");
						parameters.add("%"+ search.toLowerCase() + "%");	
					}
				);
				String lorder = ObjectHelper.useOrDefault(order, "").trim();
				ObjectHelper.runIf(
					!lorder.isEmpty(), 
					() -> {
						boolean desc = lorder.startsWith("-");
						String field = ObjectHelper.callOrElse(desc, () -> lorder.substring(1), () -> lorder);
						hql
						.append("order by c.")
						.append(field)
						.append(desc ? " desc " : " asc ");
					}
				);
				Query<Object[]> query = session.createQuery(hql.toString(), Object[].class);
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
					updateModule(module, projectModule, isActive);
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
		Future<Module> ftModule = taskHandler.submit(() ->
			trxManagerInfo.transaction((Session session) -> {
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
				updateModule(module, projectModule, isActive);
				return module;
			})
		);
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
		TimeValue timeout = TimeValue.of(TimeUnit.SECONDS, 30L);
		Module result = null;
		try {
			Module module = ftModule.get(timeout.getValue(), timeout.getUnit());
			Map<String, String> configurations = ftConfig.get(timeout.getValue(), timeout.getUnit());
			ObjectHelper.runIf(
				module != null, 
				() -> module.setConfigurations(configurations)
			);
			result = module;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw ErrorHelper.exception(e);
		} catch (Exception e) {
			throw ErrorHelper.exception(e);
		}
		return result;
	}

	
	/*
	 * FORWARD
	 */
	@Override
	public Forward getForward(AccountModuleId id) {
		Module module = getModule(id);
		ErrorHelper.throwIf(module == null, "Module is not found");
		ErrorHelper.throwIf(!ObjectHelper.isTrue(module.getIsActive()), "Module is not active");
		ErrorHelper.throwIf(!ObjectHelper.isTrue(module.getProject().getIsActive()), "Project is not active");
		Redirect redirect = module.getRedirect();
		RedirectBase processor = singletonHandler.getSingleton(
			"REDIRECT-" + redirect.getClassname(), 
			new Callable<RedirectBase>() {
				@Override
				public RedirectBase call() throws Exception {
					return ObjectHelper.newInstance(redirect.getClassname(), new Class<?>[] { ApplicationContext.class },  applicationContext);
				}
			}, 
			applicationContext.getId()
		);
		return processor.forward(module);
	}
	
}
