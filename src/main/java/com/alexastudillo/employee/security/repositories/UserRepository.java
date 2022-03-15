package com.alexastudillo.employee.security.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexastudillo.employee.security.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(final String username);
}
