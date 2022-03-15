package com.alexastudillo.employee.hr.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexastudillo.employee.handlers.ResponseHandler;
import com.alexastudillo.employee.hr.entities.Task;
import com.alexastudillo.employee.hr.repositories.TaskRepository;
import com.alexastudillo.employee.security.repositories.UserRepository;
import com.alexastudillo.employee.security.utilities.JWTTokenUtil;
import com.alexastudillo.employee.security.utilities.SecurityConstanst;

@RestController
public class TaskController {
	private final JWTTokenUtil jwtTokenUtil;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	public TaskController(final TaskRepository taskRepository, final UserRepository userRepository,
			final JWTTokenUtil jwtTokenUtil) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/api/v1/tasks/all")
	public ResponseEntity<Object> getAll() {
		final List<Task> tasks = taskRepository.findAll();
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, tasks);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
	@GetMapping("/api/v1/tasks")
	public ResponseEntity<Object> getTasksByEmployee(
			@RequestHeader(SecurityConstanst.HEADER_STRING) final String token) {
		final List<Task> tasks = taskRepository.findByEmployeeId(userRepository
				.findById(Long.parseLong(jwtTokenUtil.getUserId(token.replace(SecurityConstanst.TOKEN_PREFIX, ""))))
				.get().getEmployee().getId());
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, tasks);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/api/v1/tasks-employee")
	public ResponseEntity<Object> getTasksByEmployee(@RequestParam("employeeId") final Integer employeeId) {
		final List<Task> tasks = taskRepository.findByEmployeeId(employeeId);
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, tasks);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/api/v1/add-task")
	public ResponseEntity<Object> createTask(@RequestBody final Task task) {
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, taskRepository.save(task));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_EMPLOYEE')")
	@PostMapping("/api/v1/task-done")
	public ResponseEntity<Object> taskDone(@RequestParam("taskId") final Long id,
			@RequestParam("done") final boolean done) {
		final Task task = taskRepository.findById(id).get();
		task.setDone(done);
		return new ResponseHandler().generateResponse("successful", HttpStatus.OK, taskRepository.save(task));
	}
}
