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
@Table(name = "t_project_module")
@Setter
@Getter
@SuppressWarnings("serial")
public class ProjectModule extends EntityAudit {

	@EmbeddedId
	@AttributeOverride(name = "projectId", column = @Column(name = "project_id", nullable = false, length = 64))
	@AttributeOverride(name = "moduleId", column = @Column(name = "module_id", nullable = false, length = 64))
	private ProjectModuleId id;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "project_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_project_module__project")
	)
	private Project project;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "module_id", 
		nullable = false, 
		insertable = false, 
		updatable = false, 
		foreignKey = @ForeignKey(name = "fk_project_module__module")
	)
	private Module module;
	
	@Column(name = "name", length = 100)
	private String name;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	@Column(name = "is_active", nullable = false, length = 1)
	private Character isActive;
	
	public ProjectModule() {}
	
	public ProjectModule(ProjectModuleId id) {
		this.id = id;
	}
	
}
