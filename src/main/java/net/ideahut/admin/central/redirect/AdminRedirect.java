package net.ideahut.admin.central.redirect;

import org.springframework.context.ApplicationContext;

import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.admin.central.service.TokenService;
import net.ideahut.springboot.helper.ErrorHelper;

public class AdminRedirect extends RedirectBase {

	public AdminRedirect(ApplicationContext applicationContext) {
		super(applicationContext);
	}

	@Override
	public Forward forward(Module module) {
		String url = module.getConfiguration(String.class, "URL", "").trim();
		ErrorHelper.throwIf(url.isEmpty(), "Configuration URL is required");
		ErrorHelper.throwIf(!url.startsWith("http://") && !url.startsWith("https://"), "Invalid configuration URL");
		TokenService tokenService = getApplicationContext().getBean(TokenService.class);
		Account account = Access.get().getAccount();
		String token = tokenService.create(account.getSecurityUser());
		return new Forward()
		.setAction(url)
		.setMethod("get")
		.putParameter("_token_", token);
	}

}
