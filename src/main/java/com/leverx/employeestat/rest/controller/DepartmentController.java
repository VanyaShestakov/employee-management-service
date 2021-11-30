package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;
    private final BindingResultParser bindingResultParser;

    @Autowired
    public DepartmentController(DepartmentService departmentService, BindingResultParser bindingResultParser) {
        this.departmentService = departmentService;
        this.bindingResultParser = bindingResultParser;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DepartmentDTO getDepartment(@PathVariable("id") String id) {
        return departmentService.getById(getUUIDFromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO postDepartment(@RequestBody @Valid DepartmentDTO departmentDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Department have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return departmentService.save(departmentDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDTO putDepartment(@RequestBody @Valid DepartmentDTO departmentDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Department have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return departmentService.update(departmentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDepartment(@PathVariable("id") String id) {
        departmentService.deleteById(getUUIDFromString(id));
    }
}
