package net.ideahut.admin.central.entity;

import java.sql.Types;

import org.hibernate.annotations.JdbcTypeCode;
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
@Table(name = "t_module_configuration")
@Setter
@Getter
@SuppressWarnings("serial")
public class ModuleConfiguration extends EntityAudit {

	@EmbeddedId
	@AttributeOverride(name = "moduleId", column = @Column(name = "module_id", nullable = false, length = 64))
	@AttributeOverride(name = "redirectId", column = @Column(name = "redirect_id", nullable = false, length = 64))
	@AttributeOverride(name = "parameterName", column = @Column(name = "parameter_name", nullable = false, length = 100))
	private ModuleConfigurationId id;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "redirect_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_module_configuration__redirect")
	)
	private Redirect redirect;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(
		value = {
			@JoinColumn(name = "redirect_id", referencedColumnName = "redirect_id", nullable = false, insertable = false, updatable = false),
			@JoinColumn(name = "parameter_name", referencedColumnName = "parameter_name", nullable = false, insertable = false, updatable = false)
		},
		foreignKey = @ForeignKey(name = "fk_module_configuration__redirect_parameter")
	)
	private RedirectParameter redirectParameter;
	
	@JdbcTypeCode(Types.LONGVARCHAR)
	@Column(name = "value_")
	private String value;
	
	public ModuleConfiguration() {}
	
	public ModuleConfiguration(ModuleConfigurationId id) {
		this.id = id;
	}
	
}
