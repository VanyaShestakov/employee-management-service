package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.DuplicateDepartmentException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional
    public Department save(Department department) {
        if (departmentRepository.existsByName(department.getName())) {
            throw new DuplicateDepartmentException("Department with name=" + department.getName() + " already exists");
        }
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!departmentRepository.existsById(id)) {
            throw new NoSuchRecordException("Department with id=" + id + " not found for deleting");
        }
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Department update(Department department) {
        if (departmentRepository.existsById(department.getId())) {
            return departmentRepository.save(department);
        } else if (!departmentRepository.existsByName(department.getName())) {
            return departmentRepository.save(department);
        } else {
            throw new DuplicateDepartmentException("Department with name=" + department.getName() + " already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Department getById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Department with id=" + id + " not found");
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return departmentRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
