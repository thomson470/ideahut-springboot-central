package net.ideahut.admin.central.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.validation.Valid;
import net.ideahut.admin.central.AppProperties;
import net.ideahut.admin.central.entity.Account;
import net.ideahut.admin.central.entity.AccountModuleId;
import net.ideahut.admin.central.object.Access;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.admin.central.object.Menu;
import net.ideahut.admin.central.service.AccessService;
import net.ideahut.admin.central.service.AdminService;
import net.ideahut.springboot.annotation.Public;
import net.ideahut.springboot.context.RequestContext;
import net.ideahut.springboot.crud.CrudAction;
import net.ideahut.springboot.crud.CrudHandler;
import net.ideahut.springboot.crud.CrudPermission;
import net.ideahut.springboot.crud.CrudRequest;
import net.ideahut.springboot.crud.CrudResult;
import net.ideahut.springboot.init.InitRequest;
import net.ideahut.springboot.object.MapStringObject;
import net.ideahut.springboot.object.Page;
import net.ideahut.springboot.object.Result;
import net.ideahut.springboot.util.FrameworkUtil;
import net.ideahut.springboot.util.WebMvcUtil;

@ComponentScan
@RestController
@RequestMapping("/")
class RootController {
	
	@Autowired
	private AppProperties appProperties;
	@Autowired
	private AccessService accessService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private CrudPermission crudPermission;
	@Autowired
	private CrudHandler crudHandler;
	
	/*
	 * INDEX
	 */
	@Public
	@GetMapping
	protected void index() {
		try {
			RequestContext.currentContext().getResponse().sendRedirect(appProperties.getResource().getPath() + "/index.html");
		} catch (Exception e) {
			FrameworkUtil.exception(e);
		}
    }
	
	/*
	 * WARMUP
	 */
	@Public
	@PostMapping(
	    path = "/warmup",
	    consumes = APPLICATION_JSON_VALUE
	)
    protected ResponseEntity<String> warmup(
    	@RequestBody @Valid InitRequest initRequest
    ) {
        return ResponseEntity.ok(UUID.randomUUID().toString() + initRequest);
    }
	
	/*
	 * LOGIN
	 */
	@Public
	@PostMapping(value = "/login")
	protected Access login(
		@RequestParam("username") String username,
		@RequestParam("password") String password
	) {
		Access access = accessService.login(username, password);
		return access != null ? access.forView() : null;
	}
	
	/*
	 * LOGOUT
	 */
	@RequestMapping(value = "/logout", method = { RequestMethod.GET, RequestMethod.POST })
	protected Access logout() {
		Access access = accessService.logout(Access.get().getAuthorization());
		return access != null ? access.forView() : null;
	}
	
	/*
	 * INFO
	 */
	@Public
	@GetMapping(value = "/info")
	protected Result info() {
		Access access = Access.get().forView();
		MapStringObject config = new MapStringObject();
		config.put("multimediaUrl", appProperties.getMultimedia().getUrl());
		return Result
		.success(access)
		.setInfo("config", config);
	}
	
	/*
	 * MENUS
	 */
	@GetMapping(value = "/menus")
	protected List<Menu> menus() {
		return accessService.menus();
	}
	
	/*
	 * PROJECTS
	 */
	@PostMapping(value = "/projects")
	protected Page projects(
		@ModelAttribute Page page,
		@RequestParam(name = "search", required = false) String search,
		@RequestParam(name = "order", required = false) String order
	) {
		return adminService.getProjects(page, search, order);
	}
	
	/*
	 * MODULES
	 */
	@PostMapping(value = "/modules")
	protected Page modules(
		@RequestParam("projectId") String projectId,
		@ModelAttribute Page page,
		@RequestParam(name = "search", required = false) String search,
		@RequestParam(name = "order", required = false) String order
	) {
		return adminService.getModules(projectId, page, search, order);
	}
	
	/*
	 * GRID
	 */
	@GetMapping(value = "/grid")
	protected Result grid(
		@RequestParam("name") String name
	) {
		JsonNode grid = adminService.grid(name);
		return Result.success(grid);
	}
	
	/*
	 * CRUD
	 */
	@PostMapping(value = "/crud/{action}")
	protected Result crud(
		@PathVariable("action") String action
	) throws Exception {
		byte[] data = WebMvcUtil.getBodyAsBytes();
		CrudAction caction = CrudAction.valueOf(action.toUpperCase());
		CrudRequest crequest = crudHandler.getRequest(data);
		FrameworkUtil.throwIf(!crudPermission.isCrudAllowed(caction, crequest), "Crud is not allowed");
		CrudResult cres = crudHandler.execute(caction, crequest);
		Result result;
		if (cres.getError() != null) {
			result = Result.error(cres.getError());
		} else {
			result = Result.success(cres.getValue());
		}
		if (cres.getInfo() != null) {
			result.setInfo(cres.getInfo());
		}
		return result;
	}
	
	/*
	 * REDIRECT
	 */
	@PostMapping(value = "/redirect")
	protected Forward redirect(
		@RequestParam("projectId") String projectId,
		@RequestParam("moduleId") String moduleId
	) {
		Account account = Access.get().getAccount();
		return adminService.redirect(new AccountModuleId(account.getAccountId(), projectId, moduleId));
	}
	
}
