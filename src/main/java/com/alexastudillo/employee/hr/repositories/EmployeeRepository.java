package com.alexastudillo.employee.hr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexastudillo.employee.hr.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	public Employee findByEmail(final String email);
}
