package com.gjun.lab.security.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_roles")
public class AppRole {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 儲存「業務角色」名稱（例如 USER / ADMIN）。
	 */
	@Column(name = "name", nullable = false, unique = true, length = 64)
	private String name;

	protected AppRole() {
	}

	public AppRole(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

