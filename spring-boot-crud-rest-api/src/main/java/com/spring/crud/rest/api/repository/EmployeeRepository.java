package com.spring.crud.rest.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.crud.rest.api.model.Employee;

@Repository
public interface EmployeeRepository  extends JpaRepository<Employee, Long>
{

}
