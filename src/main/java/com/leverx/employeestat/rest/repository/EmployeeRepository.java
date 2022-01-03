package com.leverx.employeestat.rest.repository;

import com.leverx.employeestat.rest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findEmployeeById(UUID id);

    Optional<Employee> findEmployeeByUsername(String username);

    boolean existsByUsername(String username);
}
