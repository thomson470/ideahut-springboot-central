package net.ideahut.admin.central.object;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import lombok.Getter;
import lombok.Setter;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.springboot.context.RequestContext;

@Setter
@Getter
public class Access implements Serializable {
	private static final long serialVersionUID = -8934761911825153331L;
	
	private String authorization;
	private Account account;
	
	public Access forView() {
		Access access = new Access();
		BeanUtils.copyProperties(this, access);
		if (access.account != null) {
			access.account.setPassword(null);
			access.account.setEnableAudit(null);
			access.account.setEnableImageUpload(null);
			access.account.setEnableTool(null);
			access.account.setViewsByClass(null);
			access.account.setViewsByName(null);
			access.account.setSecurityUser(null);
		}
		return access;
	}
	
	public static Access of(String authorization, Account account) {
		Access access = new Access();
		access.authorization = authorization;
		access.account = account;
		return access;
	}
	
	public static Access get() {
		return RequestContext.currentContext().getAttribute(Access.class.getName(), new Access());
	}
	
	public static Access set(Access access) {
		if (access != null) {
			RequestContext.currentContext().setAttribute(Access.class.getName(), access);
		}
		return access;
	}
	
}
