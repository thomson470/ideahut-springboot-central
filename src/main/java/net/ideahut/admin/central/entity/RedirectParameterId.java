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
public class RedirectParameterId implements Serializable {
	
	@Column(name = "redirect_id", nullable = false, length = 64)
	private String redirectId;
	
	@Column(name = "parameter_name", nullable = false, length = 100)
	private String parameterName;
	
	public RedirectParameterId() {}
	
	public RedirectParameterId(String redirectId, String parameterName) {
		this.redirectId = redirectId;
		this.parameterName = parameterName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(parameterName, redirectId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedirectParameterId other = (RedirectParameterId) obj;
		return Objects.equals(parameterName, other.parameterName) && Objects.equals(redirectId, other.redirectId);
	}

}
