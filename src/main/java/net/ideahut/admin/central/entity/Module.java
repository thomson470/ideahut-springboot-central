package net.ideahut.admin.central.entity;

import java.util.Map;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import net.ideahut.springboot.annotation.Audit;
import net.ideahut.springboot.annotation.IdPrefix;
import net.ideahut.springboot.entity.EntityAudit;
import net.ideahut.springboot.generator.OdtIdGenerator;
import net.ideahut.springboot.helper.StringHelper;

@Audit
@Entity
@Table(name = "t_module")
@IdPrefix("MOD")
@Setter
@Getter
@SuppressWarnings("serial")
public class Module extends EntityAudit {

	@Id
	@GeneratedValue(generator = OdtIdGenerator.NAME)
	@GenericGenerator(name = OdtIdGenerator.NAME, type = OdtIdGenerator.class)
	@Column(name = "module_id", unique = true, nullable = false, length = 64)
	private String moduleId;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "redirect_id", 
		nullable = false, 
		foreignKey = @ForeignKey(name = "fk_module__redirect")
	)
	private Redirect redirect;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "description", length = 1024)
	private String description;
	
	@Column(name = "is_active", nullable = false, length = 1)
	private Character isActive;
	
	@Transient
	private Project project;
	@Transient
	private Map<String, String> configurations;
	
	public Module() {}
	
	public Module(String moduleId) {
		this.moduleId = moduleId;
	}
	
	@JsonIgnore
	public <T> T getConfiguration(Class<T> type, String name, T defaultValue) {
		if (configurations != null) {
			String value = configurations.get(name);
			if (value != null) {
				return StringHelper.valueOf(type, value, defaultValue);
			}
		}
		return defaultValue;
	}
	
	@JsonIgnore
	public <T> T getConfiguration(Class<T> type, String name) {
		return getConfiguration(type, name, null);
	}
	
}
