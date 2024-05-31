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
@Table(name = "t_redirect_parameter")
@Setter
@Getter
@SuppressWarnings("serial")
public class RedirectParameter extends EntityAudit {

	@EmbeddedId
	@AttributeOverride(name = "redirectId", column = @Column(name = "redirect_id", nullable = false, length = 64))
	@AttributeOverride(name = "parameterName", column = @Column(name = "parameter_name", nullable = false, length = 100))
	private RedirectParameterId id;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "redirect_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_redirect_parameter__redirect")
	)
	private Redirect redirect;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	public RedirectParameter() {}
	
	public RedirectParameter(RedirectParameterId id) {
		this.id = id;
	}
	
}
