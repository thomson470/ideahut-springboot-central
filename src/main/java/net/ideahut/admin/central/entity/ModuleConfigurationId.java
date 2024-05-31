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
public class ModuleConfigurationId implements Serializable {
	
	@Column(name = "module_id", nullable = false, length = 64)
	private String moduleId;
	
	@Column(name = "redirect_id", nullable = false, length = 64)
	private String redirectId;
	
	@Column(name = "parameter_name", nullable = false, length = 100)
	private String parameterName;
	
	public ModuleConfigurationId() {}
	
	public ModuleConfigurationId(String moduleId, String redirectId, String parameterName) {
		this.moduleId = moduleId;
		this.redirectId = redirectId;
		this.parameterName = parameterName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(moduleId, parameterName, redirectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleConfigurationId other = (ModuleConfigurationId) obj;
		return Objects.equals(moduleId, other.moduleId) && Objects.equals(parameterName, other.parameterName)
				&& Objects.equals(redirectId, other.redirectId);
	}
	
}
