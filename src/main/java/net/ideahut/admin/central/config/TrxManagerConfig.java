package net.ideahut.admin.central.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import net.ideahut.admin.central.AppConstants;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.audit.DatabaseAuditProperties;
import net.ideahut.springboot.audit.DatabaseMultiAuditHandler;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.task.TaskHandler;
import net.ideahut.springboot.util.FrameworkUtil;

@Configuration
@EnableTransactionManagement
class TrxManagerConfig {
	
	@Autowired
	private Environment environment;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	protected DataSource dataSource() {
		String jndi = environment.getProperty("spring.datasource.jndi-name", "").trim();
		if (!jndi.isEmpty()) {
			JndiDataSourceLookup lookup = new JndiDataSourceLookup();
			return lookup.getDataSource(jndi);
		} else {
			return DataSourceBuilder.create().build();
		}
    }
	
	@Bean
	protected PersistenceManagedTypes persistenceManagedTypes(
		ResourceLoader resourceLoader
	) {
		return 
		new PersistenceManagedTypesScanner(resourceLoader)
		.scan(
			AppConstants.PACKAGE + ".entity"
		);
	}
	
	@Bean
	protected LocalContainerEntityManagerFactoryBean entityManagerFactory(
		EntityManagerFactoryBuilder builder,
		PersistenceManagedTypes persistenceManagedTypes,
		DataSource dataSource
	) {
		Map<String, Object> properties = FrameworkUtil.getHibernateSettings(environment, "spring.jpa.properties");
		return builder
			.dataSource(dataSource)		
			.persistenceUnit("default")
			.managedTypes(persistenceManagedTypes)
			.properties(properties)			
			.build();
	}

	@Primary
	@Bean
	protected PlatformTransactionManager transactionManager(
		EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean
	protected AuditHandler auditHandler(
		EntityTrxManager entityTrxManager,
		TaskHandler taskHandler
	) {
		DatabaseAuditProperties.Table table = new DatabaseAuditProperties.Table();
		table.setPrefix("a_");
		DatabaseAuditProperties properties = new DatabaseAuditProperties();
		properties.setTable(table);
		return new DatabaseMultiAuditHandler()
		.setEntityTrxManager(entityTrxManager)
		.setProperties(properties)
		.setTaskHandler(taskHandler)
		.setRejectNonAuditEntity(true);
	}
	
}
