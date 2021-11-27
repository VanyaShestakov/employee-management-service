package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.converter.DepartmentConverter;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO getDepartment(@PathVariable("id") UUID id) {
        return departmentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO postDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.save(departmentDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO putDepartment(@RequestBody DepartmentDTO departmentDTO) {
        return departmentService.update(departmentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDepartment(@PathVariable("id") UUID id) {
        departmentService.deleteById(id);
    }
}
