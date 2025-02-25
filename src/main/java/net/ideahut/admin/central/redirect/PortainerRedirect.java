package net.ideahut.admin.central.redirect;

import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;
import net.ideahut.admin.central.entity.Module;
import net.ideahut.admin.central.object.Cookie;
import net.ideahut.admin.central.object.Forward;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.mapper.DataMapper;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
public class PortainerRedirect extends RedirectBase {

	private final DataMapper dataMapper;
	private final OkHttpClient defaultHttpClient;
	
	public PortainerRedirect(ApplicationContext applicationContext) {
		super(applicationContext);
		dataMapper = applicationContext.getBean(DataMapper.class);
		defaultHttpClient = new OkHttpClient.Builder().build();
	}
	
	@Override
	public Forward forward(Module module) {
		String url = module.getConfiguration(String.class, "URL", "").trim();
		ErrorHelper.throwIf(url.isEmpty(), "Configuration URL is required");
		String username = module.getConfiguration(String.class, "USERNAME", "").trim();
		ErrorHelper.throwIf(username.isEmpty(), "Configuration USERNAME is required");
		String password = module.getConfiguration(String.class, "PASSWORD", "").trim();
		ErrorHelper.throwIf(password.isEmpty(), "Configuration PASSWORD is required");
		password = FrameworkHelper.decryptFromBase64(password);
		Integer connectTimeout = module.getConfiguration(Integer.class, "TIMEOUT_CONNECT", 10);
		Integer callTimeout = module.getConfiguration(Integer.class, "TIMEOUT_CALL", 60);
		
		Forward forward = new Forward()
		.setAction(url)
		.setMethod(HttpMethod.GET.name());
		
		ObjectNode jcontent = dataMapper.createObjectNode();
		jcontent.put("username", username);
		jcontent.put("password", password);
		byte[] content = dataMapper.writeAsBytes(jcontent, DataMapper.JSON);
		Response response = null;
		try {
			OkHttpClient client = defaultHttpClient.newBuilder()
			.followRedirects(true)
			.followSslRedirects(true)
			.connectTimeout(connectTimeout, TimeUnit.SECONDS)
			.callTimeout(callTimeout, TimeUnit.SECONDS)
			.build();
			RequestBody reqBody = RequestBody.create(
				content, 
				MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
			);
			Request.Builder builder = new Request.Builder()
			.url(url + "/api/auth")
			.method(HttpMethod.POST.name(), reqBody);
			Request request = builder.build();
			Call call = client.newCall(request);
		    response = call.execute();
		    String resCookie = response.header(HttpHeaders.SET_COOKIE);
		    if (resCookie != null) {
		    	List<HttpCookie> httpCookies = HttpCookie.parse(resCookie);
		    	if (httpCookies != null) {
		    		for (HttpCookie httpCookie : httpCookies) {
		    			forward.addCookie(new Cookie().setName(httpCookie.getName()).setValue(httpCookie.getValue()));
		    		}
		    	}
		    }
		} catch (Exception e) {
			log.error("PortainerRedirect " + url, e);
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return forward;
	}

}
