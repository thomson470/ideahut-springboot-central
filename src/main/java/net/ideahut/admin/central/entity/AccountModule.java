package net.ideahut.admin.central.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.entity.EntityAudit;

@Audit
@Entity
@Table(name = "t_account_module")
@Setter
@Getter
@SuppressWarnings("serial")
public class AccountModule extends EntityAudit {

	@EmbeddedId
	@AttributeOverride(name = "accountId", column = @Column(name = "account_id", nullable = false, length = 64))
	@AttributeOverride(name = "projectId", column = @Column(name = "project_id", nullable = false, length = 64))
	@AttributeOverride(name = "moduleId", column = @Column(name = "module_id", nullable = false, length = 64))
	private AccountModuleId id;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "account_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_account_module__account")
	)
	private Account account;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "project_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_account_module__project")
	)
	private Project project;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "module_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_account_module__module")
	)
	private Module module;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
		value = {
			@JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "module_id", referencedColumnName = "module_id", nullable = false, insertable = false, updatable = false)
		},
		foreignKey = @ForeignKey(name = "fk_account_module__project_module")
	)
	private ProjectModule projectModule;
	
	@Column(name = "is_active", nullable = false, length = 1)
	private Character isActive;
	
	public AccountModule() {}
	
	public AccountModule(AccountModuleId id) {
		this.id = id;
	}
	
}
