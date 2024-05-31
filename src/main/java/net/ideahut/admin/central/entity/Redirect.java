package net.ideahut.admin.central.entity;

import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.annotation.IdPrefix;
import net.ideahut.springboot.entity.EntityAudit;
import net.ideahut.springboot.generator.OdtIdGenerator;

@Audit
@Entity
@Table(
	name = "t_redirect", 
	uniqueConstraints = @UniqueConstraint(
		columnNames = {"classname"}, 
		name = "uq_redirect__classname"
	)
)
@IdPrefix("RED")
@Setter
@Getter
@SuppressWarnings("serial")
public class Redirect extends EntityAudit {

	@Id
	@GeneratedValue(generator = OdtIdGenerator.NAME)
	@GenericGenerator(name = OdtIdGenerator.NAME, type = OdtIdGenerator.class)
	@Column(name = "redirect_id", unique = true, nullable = false, length = 64)
	private String redirectId;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "classname", nullable = false)
	private String classname;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	@Transient
	private List<RedirectParameter> parameters;
	
	public Redirect() {}
	
	public Redirect(String redirectId) {
		this.redirectId = redirectId;
	}
	
}
