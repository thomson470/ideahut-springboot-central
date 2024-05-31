package net.ideahut.admin.central.entity;

import java.util.Map;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.annotation.IdPrefix;
import net.ideahut.springboot.entity.EntityAudit;
import net.ideahut.springboot.generator.OdtIdGenerator;
import net.ideahut.springboot.security.SecurityUser;

@Audit
@Entity
@Table(
	name = "t_account", 
	indexes = { 
		@Index( name = "idx_account__username", columnList = "username")
	}
)
@IdPrefix("ACC")
@Setter
@Getter
@SuppressWarnings("serial")
public class Account extends EntityAudit {

	@Id
	@GeneratedValue(generator = OdtIdGenerator.NAME)
	@GenericGenerator(name = OdtIdGenerator.NAME, type = OdtIdGenerator.class)
	@Column(name = "account_id", unique = true, nullable = false, length = 64)
	private String accountId;
	
	@Column(name = "username", nullable = false, unique = true, length = 64)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "fullname", length = 128)
	private String fullname;
	
	@Column(name = "is_active", nullable = false, length = 1)
	private Character isActive;
	
	@Column(name = "enable_tool", nullable = false, length = 1)
	private Character enableTool;
	
	@Column(name = "enable_audit", nullable = false, length = 1)
	private Character enableAudit;
	
	@Column(name = "enable_image_upload", nullable = false, length = 1)
	private Character enableImageUpload;
	
	@Transient
	private Map<String, AccountView> viewsByName;
	@Transient
	private Map<Class<?>, AccountView> viewsByClass;
	@Transient
	private SecurityUser securityUser;
	
	public Account() {}
	
	public Account( String accountId) {
		this.accountId = accountId;
	}
	
}
