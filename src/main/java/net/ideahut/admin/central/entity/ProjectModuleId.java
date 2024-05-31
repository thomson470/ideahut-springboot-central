package net.ideahut.admin.central.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Setter 
@Getter
@SuppressWarnings("serial")
public class ProjectModuleId implements Serializable {

	@Column(name = "project_id", nullable = false, length = 64)
	private String projectId;
	
	@Column(name = "module_id", nullable = false, length = 64)
	private String moduleId;
	
	public ProjectModuleId() {}
	
	public ProjectModuleId(String projectId, String moduleId) {
		this.projectId = projectId;
		this.moduleId = moduleId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(projectId, moduleId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectModuleId other = (ProjectModuleId) obj;
		return Objects.equals(projectId, other.projectId) && Objects.equals(moduleId, other.moduleId);
	}
	
}
