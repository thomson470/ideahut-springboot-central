package net.ideahut.admin.central;

import java.io.Closeable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import lombok.extern.slf4j.Slf4j;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.bean.BeanConfigure;
import net.ideahut.springboot.bean.BeanShutdown;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.init.InitHandler;
import net.ideahut.springboot.object.ApplicationInfo;
import net.ideahut.springboot.object.VersionInfo;
import net.ideahut.springboot.task.TaskHandler;

@Slf4j
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements ApplicationListener<ContextRefreshedEvent> {
	
	public static class Package {
		private Package() {}
		public static final String LIBRARY		= FrameworkHelper.PACKAGE;
		public static final String APPLICATION	= "net.ideahut.admin.central";
	}
	
	private static Closeable closeable;
	private static boolean isClosed = false;
	private static Runnable onConfigureError = () -> {
		if (closeable != null) {
			try {
				closeable.close();
				isClosed = true;
			} catch (Exception e) {
				log.error("", e);
			}
		}
	};
	
	private static boolean ready = false;
	private static void setReady(boolean b) {
		ready = b;
	}
	public static boolean isReady() {
		return ready;
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		FrameworkHelper.checkDependencies(applicationContext);
		BeanShutdown.RuntimeHook.register(applicationContext);
		
		log.info("Configuring application '" + applicationContext.getId() + "'...");
		
		AppProperties appProperties = applicationContext.getBean(AppProperties.class);
		TaskHandler taskHandler = applicationContext.getBean(TaskHandler.class);
		taskHandler.execute(() -> {
			long time = System.currentTimeMillis();
			try {
				BeanConfigure.runBeanConfigure(
					!Boolean.FALSE.equals(appProperties.getWaitAllBeanConfigured()),
					taskHandler, 
					applicationContext,
					onConfigureError,
					EntityTrxManager.class,
					AuditHandler.class // <-- tidak async karena akan dibaca oleh AdminHandler
				);
			} catch (Exception e) {
				throw ErrorHelper.exception(e);
			}
			setReady(true);
			
			if (!isClosed) {
				log.info("Application has been configured in {} ms", System.currentTimeMillis() - time);
				runInit(taskHandler, applicationContext);
				VersionInfo versionInfo = FrameworkHelper.getVersionInfo();
				ApplicationInfo applicationInfo = FrameworkHelper.getApplicationInfo(applicationContext);
				writeInfo("Native           : ", applicationInfo.getInNativeImage());
				writeInfo("Reactive         : ", applicationInfo.getReactive());
				writeInfo("JDK              : ", versionInfo.getJava());
				writeInfo("Spring Framework : ", versionInfo.getSpringFramework());
				writeInfo("Spring Boot      : ", versionInfo.getSpringBoot());
				writeInfo("Hibernate        : ", versionInfo.getHibernate());
				writeInfo("Jedis            : ", versionInfo.getJedis());
				writeInfo("Quartz           : ", versionInfo.getQuartz());
				writeInfo("Kafka            : ", versionInfo.getKafka());
				writeInfo("Ideahut          : ", versionInfo.getIdeahut());
				int port = FrameworkHelper.getPort(applicationContext);
				log.info("Application '{}' is ready to serve on port {}", applicationContext.getId(), port);
			}
		});
	}
	
	private void writeInfo(String title, Object value) {
		if (title != null && value != null) {
			log.info("**** {} {}", title, value);
		}
	}
	
	private void runInit(TaskHandler taskHandler, ApplicationContext applicationContext) {
		taskHandler.execute(() -> {
			try {
				InitHandler initHandler = applicationContext.getBean(InitHandler.class);
				initHandler.initMapper(applicationContext);
				initHandler.initValidation();
				initHandler.initServlet();
			} catch (Exception e) {
				log.warn("InitHandler", e);
			}
		});
	}
	
	
	/*
	 * MAIN
	 */
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		application.setLazyInitialization(false);
		application.setLogStartupInfo(true);
		closeable = application.run(args);
	}
	
}
