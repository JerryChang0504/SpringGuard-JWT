package com.gjun.lab.security.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gjun.lab.security.user.entity.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String username);
}

