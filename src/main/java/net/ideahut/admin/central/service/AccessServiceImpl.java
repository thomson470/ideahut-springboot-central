package net.ideahut.admin.central.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Menu;
import net.ideahut.admin.central.object.Redis;
import net.ideahut.admin.central.object.View;
import net.ideahut.admin.central.support.MenuSupport;
import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditAccessible.AuditMember;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.WebMvcHelper;
import net.ideahut.springboot.object.TimeValue;
import net.ideahut.springboot.security.SecurityUser;

@Service
class AccessServiceImpl implements AccessService, InitializingBean {
	
	private static class Strings {
		private static final String PATH_AUDIT = new StringBuilder("/audit").toString();
	}
	private static class Prefix {
		private static final String AUTH = "AUTH-";
	}
	
	private final AppProperties appProperties;
	private final Redis redis;
	private final AccountService accountService;
	private final AuditHandler auditHandler;
	
	private TimeValue expiry;
	
	@Autowired
	AccessServiceImpl(
		AppProperties appProperties,
		Redis redis,
		AccountService accountService,
		AuditHandler auditHandler
	) {
		this.appProperties = appProperties;
		this.redis = redis;
		this.accountService = accountService;
		this.auditHandler = auditHandler;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		expiry = ObjectHelper.callOrElse(
			TimeValue.isGreaterThanZero(appProperties.getExpiry().getAuth()), 
			() -> appProperties.getExpiry().getAuth(), 
			() -> TimeValue.of(TimeUnit.MINUTES, 60L)
		);
	}

	@Override
	public Access login(String username, String password) {
		Account account = accountService.getAccountByUsername(username);
		ErrorHelper.throwIf(account == null, "LOGIN-01", "Account is not found");
		ErrorHelper.throwIf(!ObjectHelper.isTrue(account.getIsActive()), "LOGIN-02", "Account is not active");
		ErrorHelper.throwIf(!BCrypt.checkpw(password, account.getPassword()), "LOGIN-03", "Invalid password");
		String authorization = UUID.randomUUID().toString();
		HttpServletRequest request = WebMvcHelper.getRequest();
		SecurityUser user = new SecurityUser();
		user.setUsername(username);
		user.setAttribute(SecurityUser.Parameter.AUTHORIZATION, authorization);
		user.setAttribute(SecurityUser.Parameter.HOST, WebMvcHelper.getRemoteHost(request));
		user.setAttribute(SecurityUser.Parameter.AGENT, WebMvcHelper.getUserAgent(request));
		user.setAttribute("fullname", account.getFullname());
		user.setAttribute("id", account.getAccountId());
		account.setSecurityUser(user);
		Access access = Access.of(authorization, account);
		ValueOperations<String, byte[]> operations = redis.getTemplate().opsForValue();
		byte[] value = redis.getSerializer().serialize(Access.class, access);
		operations.set(redis.getPrefix() + Prefix.AUTH + authorization, value, expiry.getValue(), expiry.getUnit());
		return access;
	}
	
	@Override
	public Access logout(String authorization) {
		ValueOperations<String, byte[]> operations = redis.getTemplate().opsForValue();
		byte[] value = operations.getAndDelete(redis.getPrefix() + Prefix.AUTH + authorization);
		return ObjectHelper.callIf(value != null, () -> redis.getSerializer().deserialize(Access.class, value));
	}

	@Override
	public Access info(String authorization) {
		ValueOperations<String, byte[]> operations = redis.getTemplate().opsForValue();
		byte[] value = operations.get(redis.getPrefix() + Prefix.AUTH + authorization);
		return ObjectHelper.callIf(value != null, () -> redis.getSerializer().deserialize(Access.class, value));
	}

	@Override
	public List<Menu> menus() {
		List<Menu> menus = new ArrayList<>();
		Access access = Access.get();
		Account account = ObjectHelper.useOrDefault(access.getAccount(), Account::new);
		Map<String, AccountView> views = ObjectHelper.useOrDefault(account.getViewsByName(), LinkedHashMap::new);
		List<Menu> items = new ArrayList<>();
		menuView(items, views);
		menuEnable(items, account);
		ObjectHelper.runIf(
			!items.isEmpty(), 
			() -> {
				Menu home = new Menu()
				.setId("home")
				.setTitle("Home")
				.setLink("/")
				.setIcon("home");
				menus.add(home);
				menus.addAll(items);
			}
		);
		return menus;
	}
	
	private void menuView(
		List<Menu> items, 
		Map<String, AccountView> views
	) {
		ObjectHelper.runIf(
			views.containsKey(View.REDIRECT.name()), 
			() -> {
				Menu menu = new Menu()
				.setId("redirect")
				.setTitle("Redirect")
				.setIcon("move_up")
				.setLink("/grid?name=redirect");
				items.add(menu);
			}
		);
		ObjectHelper.runIf(
			views.containsKey(View.MODULE.name()),
			() -> {
				Menu menu = new Menu()
				.setId("module")
				.setTitle("Module")
				.setIcon("view_module")
				.setLink("/grid?name=module");
				items.add(menu);
			}
		);
		ObjectHelper.runIf(
			views.containsKey(View.PROJECT.name()),
			() -> {
				Menu menu = new Menu()
				.setId("project")
				.setTitle("Project")
				.setIcon("app_registration")
				.setLink("/grid?name=project");
				items.add(menu);
			}
		);
		ObjectHelper.runIf(
			views.containsKey(View.ACCOUNT.name()),
			() -> {
				Menu menu = new Menu()
				.setId("account")
				.setTitle("Account")
				.setIcon("manage_accounts")
				.setLink("/grid?name=account");
				items.add(menu);
			}
		);
	}
	
	private void menuEnable(
		List<Menu> items, 
		Account account
	) {
		ObjectHelper.runIf(
			ObjectHelper.isTrue(account.getEnableReload()),
			() -> {
				Menu menu = new Menu()
				.setId("reload")
				.setTitle("Reload")
				.setIcon("manage_history")
				.setLink("/reload");
				items.add(menu);
			}
		);
		ObjectHelper.runIf(
			ObjectHelper.isTrue(account.getEnableTool()),
			() -> {
				Menu menu = new Menu()
				.setId("tool")
				.setTitle("Tool")
				.setIcon("construction")
				.setLink("/tool");
				items.add(menu);
			}
		);
		ObjectHelper.runIf(
			ObjectHelper.isTrue(account.getEnableAudit()),
			() -> {
				String prefix = "audit";
				Menu mroot = new Menu()
				.setId(prefix)
				.setLink("")
				.setTitle("Audit")
				.setIcon("work_history");
				String path = Strings.PATH_AUDIT;
				Menu proot = FrameworkHelper.defaultDataMapper().copy(mroot, Menu.class);
				mroot.setChildren(new ArrayList<>());
				AuditAccessible accessible = auditHandler.getAccessibles().values().iterator().next();
				Map<Class<?>, AuditMember> members = accessible.getMembers();
				ObjectHelper.runIf(
					members != null && !members.isEmpty(), 
					() -> {
						int j = 1;
						for (AuditMember member : members.values()) {
							if (!Void.class.equals(member.getType())) {
								Menu mchild = new Menu()
								.setId(prefix + "_" + j)
								.setLink(path + "?handler=_&manager=_&type=" + member.getType().getName())
								.setTitle(member.getType().getSimpleName())
								.setIcon("av_timer")
								.setParent(proot);
								mroot.getChildren().add(mchild);
								j++;
							}
						}
						Collections.sort(mroot.getChildren(), MenuSupport.Sort.TITLE);
					}
				);
				items.add(mroot);
			}
		);
	}

}
