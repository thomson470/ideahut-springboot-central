package net.ideahut.admin.central.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.annotation.IdPrefix;
import net.ideahut.springboot.entity.EntityAudit;
import net.ideahut.springboot.generator.OdtIdGenerator;

@Audit
@Entity
@Table(name = "t_project")
@IdPrefix("PRJ")
@Setter
@Getter
@SuppressWarnings("serial")
public class Project extends EntityAudit {

	@Id
	@GeneratedValue(generator = OdtIdGenerator.NAME)
	@GenericGenerator(name = OdtIdGenerator.NAME, type = OdtIdGenerator.class)
	@Column(name = "project_id", unique = true, nullable = false, length = 64)
	private String projectId;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	@Column(name = "is_active", nullable = false, length = 1)
	private Character isActive;
	
	@Transient
	private Integer modules;
	
	public Project() {}
	
	public Project(String projectId) {
		this.projectId = projectId;
	}
	
}
