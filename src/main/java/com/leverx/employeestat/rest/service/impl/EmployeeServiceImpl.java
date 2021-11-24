package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.exception.DuplicateEmployeeException;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.repository.EmployeeRepository;
import com.leverx.employeestat.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional
    public Employee getById(UUID id) {
        return employeeRepository.findEmployeeById(id)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Employee with id=" + id + " not found");
                });
    }

    @Override
    @Transactional
    public Employee getByUsername(String username) {
        return employeeRepository.findEmployeeByUsername(username)
                .orElseThrow(() -> {
                    throw new NoSuchRecordException("Employee with username=" + username + " not found");
                });
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        if (employeeRepository.existsByUsername(employee.getUsername())) {
            throw new DuplicateEmployeeException("Employee with username=" + employee.getUsername() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee update(Employee employee) {
        if (employeeRepository.existsById(employee.getId())) {
            return employeeRepository.save(employee);
        } else if (!employeeRepository.existsByUsername(employee.getUsername())) {
            return employeeRepository.save(employee);
        } else {
            throw new DuplicateEmployeeException("Employee with username=" + employee.getUsername() + " already exists");
        }
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchRecordException("Employee with id=" + id + " not found for deleting");
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean existsById(UUID id) {
        return employeeRepository.existsById(id);
    }
}
