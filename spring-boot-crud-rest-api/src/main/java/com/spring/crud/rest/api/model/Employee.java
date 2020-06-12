package com.spring.crud.rest.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.springframework.cache.annotation.CacheConfig;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "TEMP_EMPLOYEE")
@SequenceGenerator(name="TEMP_EMPLOYEE_SEQ", initialValue=1, allocationSize=1)
public class Employee 
{
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="TEMP_EMPLOYEE_SEQ")
	@ApiModelProperty(notes = "The database generated employee ID")
	@Column(name = "id")
	private long id;
	
	@ApiModelProperty(notes = "first Name of employee")
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@ApiModelProperty(notes = "last Name of employee")
	@Column(name = "last_name", nullable = false)
    private String lastName;
	
	@ApiModelProperty(notes = "email address of employee")
	@Column(name = "email_address", nullable = false)
    private String emailId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ "]";
	}
	
	
	
}
