package net.ideahut.admin.central.redirect;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;
import net.ideahut.admin.central.entity.Module;
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

	public PortainerRedirect(ApplicationContext applicationContext) {
		super(applicationContext);
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
		
		DataMapper dataMapper = getApplicationContext().getBean(DataMapper.class);
		ObjectNode jcontent = dataMapper.createObjectNode();
		jcontent.put("username", username);
		jcontent.put("password", password);
		byte[] content = dataMapper.writeAsBytes(jcontent, DataMapper.JSON);
		
		TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        @Override
		        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        	/**/
		        }
		        @Override
		        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        	/**/
		        }
		        @Override
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return new java.security.cert.X509Certificate[0];
		        }
		    }
		};
		Response response = null;
		String jwt = null;
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			
			OkHttpClient client = new OkHttpClient()
			.newBuilder()
			.followRedirects(true)
			.followSslRedirects(true)
			.connectTimeout(10, TimeUnit.SECONDS)
			.callTimeout(60, TimeUnit.SECONDS)
			.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
			.hostnameVerifier((hostname, session) -> true)
			.build();
			RequestBody reqBody = RequestBody.create(content, MediaType.parse("application/json"));
			Request.Builder builder = new Request.Builder()
			.url(url + "/api/auth")
			.method("POST", reqBody);
			Request request = builder.build();
			Call call = client.newCall(request);
		    response = call.execute();
		    byte[] respBody = response.body().bytes();
		    JsonNode node = dataMapper.read(respBody, JsonNode.class);
		    if (node.has("jwt")) {
		    	jwt = node.get("jwt").asText();
		    }
		} catch (Exception e) {
			log.error("PortainerRedirect " + url, e);
			//throw ErrorHelper.exception(e);//-
		} finally {
			if (response != null) {
				response.close();
			}
		}
		Forward forward = new Forward();
		forward.setAction(url);
		forward.setMethod("get");
		if (jwt != null && !jwt.isEmpty()) {
			forward.setParameter("access_token", URLEncoder.encode(jwt, StandardCharsets.UTF_8));
		}
		return forward;
	}
	
	//public static void main(String...args) throws Exception {
		/*
		HtmlUnitHandler handler = new HtmlUnitHandlerImpl();
		HtmlUnitConfig config = new HtmlUnitConfig();
		config.setCookieEnabled(true);
		HtmlUnitRequest request = new HtmlUnitRequest();
		request.setUrl("https://localhost:9443");
		HtmlUnitResponse response = handler.browse(config, request, false);
		System.out.println(response.getBody());
		System.out.println(response.getHeaders());
		*/
		
		/*
		HtmlUnitService service = new HtmlUnitServiceImpl();
		HtmlUnitConfig config = new HtmlUnitConfig();
		config.setCookieEnabled(true);
		WebClient client = service.webClient(config);
		HtmlPage page = client.getPage("https://localhost:9443");
		page.getWebResponse().c
		
		//HtmlUnitRequest request = new HtmlUnitRequest();
		//request.setUrl("https://localhost:9443");
		//HtmlUnitResponse response = service.browse(null, request, true);
		//System.out.println(response.getPage().getcoo);
		 */
		/*
		URL url = new URL("https://localhost:9443");
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setConnectTimeout(5_000);
		con.setReadTimeout(60_000);
		con.setRequestMethod(HttpMethod.GET.name());
		con.setDoOutput(true);
		con.setHostnameVerifier((hostname, session) -> true);
		int status = con.getResponseCode();
		System.out.println(status);
		*/
		
		
		/*
		DataMapper dataMapper = new DataMapperImpl();
		ObjectNode jcontent = dataMapper.createObjectNode();
		jcontent.put("username", "admin");
		jcontent.put("password", "4dm1nku4dm1nku");
		byte[] content = dataMapper.writeAsBytes(jcontent, DataMapper.JSON);
		
		TrustManager[] trustAllCerts = new TrustManager[]{
		    new X509TrustManager() {
		        @Override
		        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        }
		        @Override
		        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
		        }
		        @Override
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return new java.security.cert.X509Certificate[0];
		        	//return null;
		        }
		    }
		};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		
		OkHttpClient client = new OkHttpClient()
		.newBuilder()
		.connectTimeout(10, TimeUnit.SECONDS)
		.callTimeout(60, TimeUnit.SECONDS)
		.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
		.hostnameVerifier((hostname, session) -> true)
		.build();
		RequestBody reqBody = RequestBody.create(content, MediaType.parse("application/json"));
		Request.Builder builder = new Request.Builder()
		//.url("https://localhost:9443/api/auth")
		.url("https://localhost:9443/api/auth")
		.method("POST", reqBody);
		Request request = builder.build();
		Call call = client.newCall(request);
		Response response = call.execute();
	    byte[] respBody = response.body().bytes();
	    String jwt = dataMapper.read(respBody, JsonNode.class).get("jwt").asText();
	    response.close();
	    System.out.println(jwt);
	    
	    builder = new Request.Builder()
		.url("https://localhost:9443")
		.header("Authorization", "Bearer " + jwt)
		.get();
	    request = builder.build();
	    call = client.newCall(request);
	    response = call.execute();
	    Headers headers = response.headers();
	    Iterator<Pair<String, String>> iter = headers.iterator();
	    while(iter.hasNext()) {
	    	Pair<String, String> pair = iter.next();
	    	System.out.println(pair.getFirst() + "=" + pair.getSecond());
	    }
	    respBody = response.body().bytes();
	    
	    
	    /*
	    System.out.println(response.header("Set-Cookie"));
	    
	    Headers headers = response.headers();
	    Iterator<Pair<String, String>> iter = headers.iterator();
	    while(iter.hasNext()) {
	    	Pair<String, String> pair = iter.next();
	    	System.out.println(pair.getFirst() + "=" + pair.getSecond());
	    }
	    
	    String respText = new String(respBody);
	    int status = response.code();
	    response.close();
	    System.out.println("status: " + status);
	    System.out.println("respText: " + respText);
	    */
	//}

}
