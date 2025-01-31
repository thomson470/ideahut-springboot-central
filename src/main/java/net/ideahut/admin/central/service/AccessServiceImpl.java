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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import net.ideahut.admin.central.AppConstants;
import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Menu;
import net.ideahut.admin.central.object.View;
import net.ideahut.admin.central.support.MenuSupport;
import net.ideahut.springboot.audit.AuditAccessible;
import net.ideahut.springboot.audit.AuditAccessible.AuditMember;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.helper.WebMvcHelper;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.object.TimeValue;
import net.ideahut.springboot.security.SecurityUser;

@Service
class AccessServiceImpl implements AccessService, InitializingBean {
	
	private static class Strings {
		private static final String PATH_AUDIT = new StringBuilder("/audit").toString();
	}
	
	private final AppProperties appProperties;
	private final DataMapper dataMapper;
	private final RedisTemplate<String, byte[]> redisTemplate;
	private final AccountService accountService;
	private final AuditHandler auditHandler;
	
	private TimeValue expiry;
	
	@Autowired
	AccessServiceImpl(
		AppProperties appProperties,
		DataMapper dataMapper,
		RedisTemplate<String, byte[]> redisTemplate,
		AccountService accountService,
		AuditHandler auditHandler
	) {
		this.appProperties = appProperties;
		this.dataMapper = dataMapper;
		this.redisTemplate = redisTemplate;
		this.accountService = accountService;
		this.auditHandler = auditHandler;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (!TimeValue.isGreaterThanZero(appProperties.getExpiry().getAuth())) {
			expiry = TimeValue.of(TimeUnit.MINUTES, 60L);
		} else {
			expiry = appProperties.getExpiry().getAuth();
		}
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
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = dataMapper.writeAsBytes(access, DataMapper.JSON);
		operations.set(AppConstants.Prefix.AUTH + authorization, value, expiry.getValue(), expiry.getUnit());
		return access;
	}
	
	@Override
	public Access logout(String authorization) {
		Access access = null;
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = operations.getAndDelete(AppConstants.Prefix.AUTH + authorization);
		if (value != null) {
			access = dataMapper.read(value, Access.class);
		}
		return access;
	}

	@Override
	public Access info(String authorization) {
		Access access = null;
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = operations.get(AppConstants.Prefix.AUTH + authorization);
		if (value != null) {
			access = dataMapper.read(value, Access.class);
		}
		return access;
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
		if (!items.isEmpty()) {
			Menu home = new Menu();
			home.setId("home");
			home.setTitle("Home");
			home.setLink("/");
			home.setIcon("home");
			menus.add(home);
			menus.addAll(items);
		}
		return menus;
	}
	
	private void menuView(
		List<Menu> items, 
		Map<String, AccountView> views
	) {
		if (views.containsKey(View.ACCOUNT.name())) {
			Menu menu = new Menu();
			menu.setId("account");
			menu.setTitle("Account");
			menu.setIcon("manage_accounts");
			menu.setLink("/grid?name=account");
			items.add(menu);
		}
		if (views.containsKey(View.REDIRECT.name())) {
			Menu menu = new Menu();
			menu.setId("redirect");
			menu.setTitle("Redirect");
			menu.setIcon("move_up");
			menu.setLink("/grid?name=redirect");
			items.add(menu);
		}
		if (views.containsKey(View.PROJECT.name())) {
			Menu menu = new Menu();
			menu.setId("project");
			menu.setTitle("Project");
			menu.setIcon("app_registration");
			menu.setLink("/grid?name=project");
			items.add(menu);
		}
		if (views.containsKey(View.MODULE.name())) {
			Menu menu = new Menu();
			menu.setId("module");
			menu.setTitle("Module");
			menu.setIcon("view_module");
			menu.setLink("/grid?name=module");
			items.add(menu);
		}
	}
	
	private void menuEnable(
		List<Menu> items, 
		Account account
	) {
		if (ObjectHelper.isTrue(account.getEnableReload())) {
			Menu menu = new Menu();
			menu.setId("reload");
			menu.setTitle("Reload");
			menu.setIcon("manage_history");
			menu.setLink("/reload");
			items.add(menu);
		}
		if (ObjectHelper.isTrue(account.getEnableTool())) {
			Menu menu = new Menu();
			menu.setId("tool");
			menu.setTitle("Tool");
			menu.setIcon("construction");
			menu.setLink("/tool");
			items.add(menu);
		}
		if (ObjectHelper.isTrue(account.getEnableAudit())) {
			String prefix = "audit";
			Menu mroot = new Menu();
			mroot.setId(prefix);
			mroot.setLink("");
			mroot.setTitle("Audit");
			mroot.setIcon("work_history");
			String path = Strings.PATH_AUDIT;
			Menu proot = dataMapper.copy(mroot, Menu.class);
			mroot.setChildren(new ArrayList<>());
			
			AuditAccessible accessible = auditHandler.getAccessibles().values().iterator().next();
			Map<Class<?>, AuditMember> members = accessible.getMembers();
			if (members != null && !members.isEmpty()) {
				int j = 1;
				for (AuditMember member : members.values()) {
					if (!Void.class.equals(member.getType())) {
						Menu mchild = new Menu();
						mchild.setId(prefix + "_" + j);
						mchild.setLink(path + "?handler=_&manager=_&type=" + member.getType().getName());
						mchild.setTitle(member.getType().getSimpleName());
						mchild.setIcon("av_timer");
						mchild.setParent(proot);
						mroot.getChildren().add(mchild);
						j++;
					}
				}
				Collections.sort(mroot.getChildren(), MenuSupport.Sort.TITLE);
			}
			items.add(mroot);
		}
	}

}
