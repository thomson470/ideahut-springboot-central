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
public class AccountViewId implements Serializable {

	@Column(name = "account_id", nullable = false, length = 64)
	private String accountId;
	
	@Column(name = "view_name", nullable = false, length = 64)
	private String viewName;
	
	public AccountViewId() {}
	
	public AccountViewId(String accountId, String viewName) {
		this.accountId = accountId;
		this.viewName = viewName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, viewName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountViewId other = (AccountViewId) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(viewName, other.viewName);
	}
	
}
