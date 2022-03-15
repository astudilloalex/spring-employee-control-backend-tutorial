package com.alexastudillo.employee.hr.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.alexastudillo.employee.security.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "employees")
public class Employee implements Serializable {
	private static final long serialVersionUID = 8650829426351290783L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;

	@Column(name = "email", nullable = false, unique = true, length = 50)
	private String email;

	@Column(name = "active", nullable = false)
	private boolean active = true;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL", updatable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL")
	@UpdateTimestamp
	private Timestamp updateDate;

	@OneToOne(mappedBy = "employee", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("employee")
	private User user;

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public Employee(final String firstName, final String lastName, final String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
