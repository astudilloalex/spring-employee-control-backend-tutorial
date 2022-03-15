package com.alexastudillo.employee.security.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "roles")
public class Role implements Serializable {
	private static final long serialVersionUID = -6513805517976937674L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "name", nullable = false, length = 15, unique = true)
	private String name;

	@Column(name = "active", nullable = false)
	private boolean active = true;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL", updatable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL")
	@UpdateTimestamp
	private Timestamp updateDate;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();

	public Role() {
		// TODO Auto-generated constructor stub
	}

	public Role(final String name) {
		this.name = name;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
