package com.leverx.employeestat.rest.repository;

import com.leverx.employeestat.rest.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    @Override
    Optional<Department> findById(UUID uuid);

    boolean existsById(UUID uuid);

    boolean existsByName(String name);

}
