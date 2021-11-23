package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.RecordNotFoundException;
import com.leverx.employeestat.rest.repository.DepartmentRepository;
import com.leverx.employeestat.rest.service.DepartmentService;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Department update(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }


    @Override
    @Transactional
    public Department getById(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> {throw new RecordNotFoundException("Department with id=" + id + " not found");});
    }

    @Override
    @Transactional
    public boolean existsById(UUID id) {
        return departmentRepository.existsById(id);
    }

    @Override
    @Transactional
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
}
