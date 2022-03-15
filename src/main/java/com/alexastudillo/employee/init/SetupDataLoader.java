package com.alexastudillo.employee.init;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alexastudillo.employee.hr.entities.Employee;
import com.alexastudillo.employee.hr.repositories.EmployeeRepository;
import com.alexastudillo.employee.security.entities.Role;
import com.alexastudillo.employee.security.entities.User;
import com.alexastudillo.employee.security.repositories.RoleRepository;
import com.alexastudillo.employee.security.repositories.UserRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
	private boolean alreadySetup = false;

	private final EmployeeRepository employeeRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SetupDataLoader(EmployeeRepository employeeRepository, RoleRepository roleRepository,
			UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup)
			return;
		final Role roleAdmin = createRole("ROLE_ADMIN");
		createRole("ROLE_EMPLOYEE");
		User user = userRepository.findByUsername("admin");
		if (user == null) {
			user = new User();
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("alexastudillo"));
			user.setEmployee(createEmployee("Alex", "Astudillo", "alex@gmail.com"));
			user.setRoles(new HashSet<Role>(Arrays.asList(roleAdmin)));
			userRepository.save(user);
		}
		alreadySetup = true;
	}

	@Transactional
	private Employee createEmployee(final String firstName, final String lastName, final String email) {
		Employee employee = employeeRepository.findByEmail(email);
		if (employee == null)
			employee = employeeRepository.save(new Employee(firstName, lastName, email));
		return employee;
	}

	@Transactional
	private Role createRole(final String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = roleRepository.save(new Role(name));
		}
		return role;
	}

}
