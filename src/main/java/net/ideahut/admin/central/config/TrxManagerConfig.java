package net.ideahut.admin.central.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import net.ideahut.admin.central.AppConstants;
import net.ideahut.admin.central.AppProperties;
import net.ideahut.springboot.audit.AuditHandler;
import net.ideahut.springboot.audit.DatabaseMultiAuditHandler;
import net.ideahut.springboot.definition.DatabaseAuditDefinition;
import net.ideahut.springboot.entity.EntityTrxManager;
import net.ideahut.springboot.helper.ObjectHelper;
import net.ideahut.springboot.task.TaskHandler;

@Configuration
@EnableTransactionManagement
class TrxManagerConfig {
	
	@Bean
	DataSource dataSource(
		AppProperties appProperties	
	) {
		HikariConfig config = new HikariConfig(appProperties.getDatasource());
		return new HikariDataSource(config);
    }
	
	@Bean
	PersistenceManagedTypes persistenceManagedTypes(
		ResourceLoader resourceLoader
	) {
		return 
		new PersistenceManagedTypesScanner(resourceLoader)
		.scan(
			AppConstants.PACKAGE + ".entity"
		);
	}
	
	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(
		AppProperties appProperties,
		EntityManagerFactoryBuilder builder,
		PersistenceManagedTypes persistenceManagedTypes,
		DataSource dataSource
	) {
		Properties hibernate = ObjectHelper.useOrDefault(appProperties.getHibernate(), Properties::new);
		Map<String, Object> properties = new HashMap<>();
		for (Map.Entry<Object, Object> entry : hibernate.entrySet()) {
			properties.put("hibernate." + entry.getKey(), entry.getValue());
		}
		return builder
			.dataSource(dataSource)	
			.managedTypes(persistenceManagedTypes)
			.properties(properties)			
			.build();
	}

	@Bean
	PlatformTransactionManager transactionManager(
		EntityManagerFactory entityManagerFactory
	) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean
	AuditHandler auditHandler(
		AppProperties appProperties,
		EntityTrxManager entityTrxManager,
		TaskHandler taskHandler
	) {
		DatabaseAuditDefinition audit = appProperties.getAudit();
		return new DatabaseMultiAuditHandler()
		.setEntityTrxManager(entityTrxManager)
		.setProperties(audit.getProperties())
		.setRejectNonAuditEntity(!Boolean.FALSE.equals(audit.getRejectNonAuditEntity()))
		.setTaskHandler(taskHandler);
	}
	
}
