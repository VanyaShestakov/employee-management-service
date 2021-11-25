package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.dto.converter.DepartmentConverter;
import com.leverx.employeestat.rest.entity.Department;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController("department")
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentConverter departmentConverter;

    @Autowired
    public DepartmentController(DepartmentService departmentService, DepartmentConverter departmentConverter) {
        this.departmentService = departmentService;
        this.departmentConverter = departmentConverter;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAll()
                .stream()
                .map(departmentConverter::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO postDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentConverter.toEntity(departmentDTO);
        return departmentConverter.toDTO(departmentService.save(department));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO putDepartment(@RequestBody DepartmentDTO departmentDTO) {
        Department department = departmentConverter.toEntity(departmentDTO);
        return departmentConverter.toDTO(departmentService.update(department));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDepartment(@PathVariable("id") UUID id) {
        departmentService.deleteById(id);
    }
}
