package com.alexastudillo.employee.security.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.alexastudillo.employee.hr.entities.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
public class User implements UserDetails {
	private static final long serialVersionUID = 1448969406530133503L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", unique = true, length = 50, nullable = false)
	private String username;

	@Column(name = "password", nullable = false, length = 128)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Column(name = "account_non_locked", nullable = false)
	private boolean accountNonLocked = true;

	@Column(name = "account_non_expired", nullable = false)
	private boolean accountNonExpired = true;

	@Column(name = "credentials_non_expired", nullable = false)
	private boolean credentialsNonExpired = true;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@Column(name = "creation_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL", updatable = false)
	@CreationTimestamp
	private Timestamp creationDate;

	@Column(name = "update_date", columnDefinition = "TIMESTAMP WITH TIME ZONE NOT NULL")
	@UpdateTimestamp
	private Timestamp updateDate;

	@OneToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
	@JsonIgnoreProperties("user")
	private Employee employee;

	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = {
			@JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonIgnoreProperties("users")
	private Set<Role> roles = new HashSet<Role>();

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	public User(final String username, final String password, final Role role) {
		this.username = username;
		this.password = password;
		roles.add(role);
	}

	public User(final String username, final String password, final Set<Role> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public User(final String username, final String password, final Role role, final Employee employee) {
		this.username = username;
		this.password = password;
		this.employee = employee;
		roles.add(role);
	}

	public void addRole(Role role) {
		roles.add(role);
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (final Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
