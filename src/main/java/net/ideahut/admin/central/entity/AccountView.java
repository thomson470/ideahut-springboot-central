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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.entity.EntityAudit;

@Audit
@Entity
@Table(name = "t_account_view")
@Setter
@Getter
@SuppressWarnings("serial")
public class AccountView extends EntityAudit {

	@EmbeddedId
	@AttributeOverride(name = "accountId", column = @Column(name = "account_id", nullable = false, length = 64))
	@AttributeOverride(name = "viewName", column = @Column(name = "view_name", nullable = false, length = 64))
	private AccountViewId id;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "account_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_account_view__account")
	)
	private Account account;
	
	@Column(name = "enable_create", nullable = false, length = 1)
	private Character enableCreate;
	
	@Column(name = "enable_retrieve", nullable = false, length = 1)
	private Character enableRetrieve;
	
	@Column(name = "enable_update", nullable = false, length = 1)
	private Character enableUpdate;
	
	@Column(name = "enable_delete", nullable = false, length = 1)
	private Character enableDelete;
	
	
}
