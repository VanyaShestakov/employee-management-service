package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.DuplicateDepartmentException;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAllDepartments() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Department> postDepartment(@RequestBody Department department) {
        if (departmentService.existsByName(department.getName())) {
            throw new DuplicateDepartmentException("Department with name=" + department.getName() + " already exists");
        }
        return new ResponseEntity<>(departmentService.save(department), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Department> putDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.save(department), HttpStatus.OK);
    }
}
