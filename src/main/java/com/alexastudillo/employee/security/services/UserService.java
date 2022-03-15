package com.alexastudillo.employee.security.services;

import java.util.HashSet;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alexastudillo.employee.security.entities.Role;
import com.alexastudillo.employee.security.entities.User;
import com.alexastudillo.employee.security.repositories.RoleRepository;
import com.alexastudillo.employee.security.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public UserService(final RoleRepository roleRepository, final UserRepository userRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("username-does-not-exist");
		}
		user.setRoles(new HashSet<Role>(roleRepository.findByUserId(user.getId())));
		return user;
	}

}
