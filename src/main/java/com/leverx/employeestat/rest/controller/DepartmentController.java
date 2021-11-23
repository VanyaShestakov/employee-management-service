package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController("department")
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
        return new ResponseEntity<>(departmentService.save(department), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Department> putDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.save(department), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable("id") UUID id) {
        departmentService.deleteById(id);
    }
}
