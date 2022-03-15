package com.alexastudillo.employee.hr.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alexastudillo.employee.handlers.ResponseHandler;
import com.alexastudillo.employee.hr.entities.Employee;
import com.alexastudillo.employee.hr.repositories.EmployeeRepository;
import com.alexastudillo.employee.security.entities.User;
import com.alexastudillo.employee.security.repositories.RoleRepository;
import com.alexastudillo.employee.security.repositories.UserRepository;

@RestController
public class EmployeeController {
	private final EmployeeRepository employeeRepository;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public EmployeeController(final EmployeeRepository employeeRepository, final RoleRepository roleRepository,
			final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
		this.employeeRepository = employeeRepository;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/api/v1/employees/all")
	public ResponseEntity<Object> getAll() {
		final List<Employee> employees = employeeRepository.findAll();
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, employees);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/api/v1/add-employee")
	public ResponseEntity<Object> createEmployee(@RequestBody final Employee employee) {
		final Employee employeeSave = employeeRepository.save(employee);
		final String password = "password1234";
		final User user = userRepository.save(
				new User(employeeSave.getEmail(), passwordEncoder.encode(password), roleRepository.findByName("ROLE_EMPLOYEE"), employeeSave));
		final Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("status", 200);
		infoMap.put("message", "successful");
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("temporalPassword", password);
		data.put("userInfo", user);
		infoMap.put("data", data);
		return new ResponseHandler().generateResponse(infoMap, HttpStatus.OK);
	}

}
