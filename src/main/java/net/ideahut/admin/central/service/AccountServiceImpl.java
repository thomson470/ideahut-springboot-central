package net.ideahut.admin.central.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountModule;
import net.ideahut.admin.central.entity.AccountView;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.entity.ModuleConfiguration;
import net.ideahut.admin.central.entity.Project;
import net.ideahut.admin.central.entity.ProjectModule;
import net.ideahut.admin.central.entity.Redirect;
import net.ideahut.admin.central.entity.RedirectParameter;
import net.ideahut.admin.central.object.View;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.entity.SessionCallable;
import net.ideahut.springboot.entity.TrxManagerInfo;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private EntityTrxManager entityTrxManager;

	@Override
	public Account getAccountByUsername(String username) {
		TrxManagerInfo trxManagerInfo = entityTrxManager.getDefaultTrxManagerInfo();
		return trxManagerInfo.transaction(new SessionCallable<Account>() {
			@Override
			public Account call(Session session) throws Exception {
				Account account = session.createQuery(
					"from Account " +
					"where username=?1 ",
					Account.class
				)
				.setParameter(1, username)
				.uniqueResult();
				trxManagerInfo.loadLazy(account, Account.class);
				if (account != null) {
					List<AccountView> items = trxManagerInfo.transaction(new SessionCallable<List<AccountView>>() {
						@Override
						public List<AccountView> call(Session session) throws Exception {
							List<AccountView> list = session.createQuery(
								"from AccountView where id.accountId=?1",
								AccountView.class
							)
							.setParameter(1, account.getAccountId())
							.getResultList();
							trxManagerInfo.loadLazy(list, AccountView.class);
							return list;
						}
					});
					account.setViewsByClass(new LinkedHashMap<>());
					account.setViewsByName(new LinkedHashMap<>());
					while (!items.isEmpty()) {
						AccountView crud = items.remove(0);
						trxManagerInfo.nullAudit(crud);
						View view = View.of(crud.getId().getViewName());
						if (view != null) {
							account.getViewsByClass().put(view.getType(), crud);
							account.getViewsByName().put(view.name(), crud);
						}
					}
					AccountView crudAccount = account.getViewsByClass().get(Account.class);
					if (crudAccount != null) {
						account.getViewsByClass().put(AccountView.class, crudAccount);
						account.getViewsByClass().put(AccountModule.class, crudAccount);
					}
					AccountView crudRedirect = account.getViewsByClass().get(Redirect.class);
					if (crudRedirect != null) {
						account.getViewsByClass().put(RedirectParameter.class, crudRedirect);
					}
					AccountView crudModule = account.getViewsByClass().get(Module.class);
					if (crudModule != null) {
						account.getViewsByClass().put(ModuleConfiguration.class, crudModule);
					}
					AccountView crudProject = account.getViewsByClass().get(Project.class);
					if (crudProject != null) {
						account.getViewsByClass().put(ProjectModule.class, crudProject);
					}
				}
				return account;
			}
		});
	}
	
}
