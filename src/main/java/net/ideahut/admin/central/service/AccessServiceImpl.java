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
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.security.SecurityUser;
import net.ideahut.springboot.util.FrameworkUtil;
import net.ideahut.springboot.util.WebMvcUtil;

@Service
public class AccessServiceImpl implements AccessService, InitializingBean {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private DataMapper dataMapper;
	@Autowired
	private RedisTemplate<String, byte[]> redisTemplate;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuditHandler auditHandler;
	
	private Integer authExpiry = 3600;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		AppProperties.Expiry expiry = appProperties.getExpiry();
		if (expiry.getAuth() != null && expiry.getAuth() > 0) {
			authExpiry = expiry.getAuth();
		}
	}

	@Override
	public Access login(String username, String password) {
		Account account = accountService.getAccountByUsername(username);
		FrameworkUtil.throwIf(account == null, "LOGIN-01", "Account is not found");
		FrameworkUtil.throwIf(!FrameworkUtil.isTrue(account.getIsActive()), "LOGIN-02", "Account is not active");
		FrameworkUtil.throwIf(!BCrypt.checkpw(password, account.getPassword()), "LOGIN-03", "Invalid password");
		String authorization = UUID.randomUUID().toString();
		SecurityUser user = new SecurityUser();
		user.setUsername(username);
		user.setAttribute(SecurityUser.Parameter.AUTHORIZATION, authorization);
		user.setAttribute(SecurityUser.Parameter.HOST, WebMvcUtil.getRemoteHost());
		user.setAttribute(SecurityUser.Parameter.AGENT, WebMvcUtil.getUserAgent());
		user.setAttribute("fullname", account.getFullname());
		user.setAttribute("id", account.getAccountId());
		account.setSecurityUser(user);
		Access access = Access.of(authorization, account);
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = dataMapper.writeAsBytes(access, DataMapper.JSON);
		operations.set(AppConstants.Prefix.AUTH + authorization, value, authExpiry, TimeUnit.SECONDS);
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
		Account account = FrameworkUtil.getOrDefault(access.getAccount(), new Account());
		Map<String, AccountView> views = FrameworkUtil.getOrDefault(account.getViewsByName(), new LinkedHashMap<>());
		List<Menu> items = new ArrayList<>();
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
		if (FrameworkUtil.isTrue(account.getEnableTool())) {
			Menu menu = new Menu();
			menu.setId("tool");
			menu.setTitle("Tool");
			menu.setIcon("construction");
			menu.setLink("/tool");
			items.add(menu);
		}
		if (FrameworkUtil.isTrue(account.getEnableAudit())) {
			String prefix = "audit";
			Menu mroot = new Menu();
			mroot.setId(prefix);
			mroot.setLink("");
			mroot.setTitle("Audit");
			mroot.setIcon("work_history");
			String path =  "/audit";
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

}
