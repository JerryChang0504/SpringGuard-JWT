package com.gjun.lab.security.user.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false, unique = true, length = 128)
	private String username;

	@Column(name = "password_hash", nullable = false, length = 200)
	private String passwordHash;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "app_user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<AppRole> roles = new HashSet<>();

	protected AppUser() {
	}

	public AppUser(String username, String passwordHash, boolean enabled) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Set<AppRole> getRoles() {
		return roles;
	}
}

