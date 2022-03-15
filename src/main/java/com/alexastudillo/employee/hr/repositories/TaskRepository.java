package com.alexastudillo.employee.hr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.alexastudillo.employee.hr.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query(value = "SELECT * FROM tasks WHERE employee_id=:eid", nativeQuery = true)
	public List<Task> findByEmployeeId(@Param("eid") final Integer eid);
}
