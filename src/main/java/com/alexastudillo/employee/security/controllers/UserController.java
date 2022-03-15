package com.alexastudillo.employee.security.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.alexastudillo.employee.handlers.ResponseHandler;
import com.alexastudillo.employee.security.entities.User;
import com.alexastudillo.employee.security.repositories.UserRepository;
import com.alexastudillo.employee.security.utilities.JWTTokenUtil;
import com.alexastudillo.employee.security.utilities.SecurityConstanst;

@RestController
public class UserController {
	private final UserRepository userRepository;
	private final JWTTokenUtil jwtTokenUtil;

	public UserController(UserRepository userRepository, JWTTokenUtil jwtTokenUtil) {
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/api/v1/users/all")
	public ResponseEntity<Object> getAll() {
		final List<User> users = userRepository.findAll();
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, users);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/api/v1/user")
	public ResponseEntity<Object> getUser(@RequestHeader(SecurityConstanst.HEADER_STRING) final String token) {
		final User user = userRepository
				.findById(Long.parseLong(jwtTokenUtil.getUserId(token.replace(SecurityConstanst.TOKEN_PREFIX, ""))))
				.get();
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, user);
	}
}
