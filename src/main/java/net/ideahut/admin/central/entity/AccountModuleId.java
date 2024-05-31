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
public class AccountModuleId implements Serializable {

	@Column(name = "account_id", nullable = false, length = 64)
	private String accountId;
	
	@Column(name = "project_id", nullable = false, length = 64)
	private String projectId;
	
	@Column(name = "module_id", nullable = false, length = 64)
	private String moduleId;
	
	public AccountModuleId() {}
	
	public AccountModuleId(String accountId, String projectId, String moduleId) {
		this.accountId = accountId;
		this.projectId = projectId;
		this.moduleId = moduleId;
	}
	
	public AccountModuleId(String accountId, ProjectModuleId projectModuleId) {
		this.accountId = accountId;
		if (projectModuleId != null) {
			this.projectId = projectModuleId.getProjectId();
			this.moduleId = projectModuleId.getModuleId();
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, moduleId, projectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountModuleId other = (AccountModuleId) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(moduleId, other.moduleId)
				&& Objects.equals(projectId, other.projectId);
	}
	
}
