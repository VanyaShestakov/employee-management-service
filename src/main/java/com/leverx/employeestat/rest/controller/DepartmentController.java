package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.controller.tool.BindingResultParser;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
import com.leverx.employeestat.rest.exception.NotValidRecordException;
import com.leverx.employeestat.rest.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/departments")
@Api(tags = "Department CRUD operations")
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
    @ApiOperation(value = "Get list of all departments")
    public List<DepartmentDTO> getAllDepartments() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get department by id")
    public DepartmentDTO getDepartment(@ApiParam(value = "Id of reсeiving department (UUID)")
                                       @PathVariable("id") String id) {
        return departmentService.getById(getUUIDFromString(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create department")
    public DepartmentDTO postDepartment(@ApiParam(value = "Contains mandatory information of department", name = "Department")
                                        @RequestBody
                                        @Valid DepartmentDTO departmentDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Department have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return departmentService.save(departmentDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update department")
    public DepartmentDTO putDepartment(@ApiParam(value = "Contains all information of department with changed fields", name = "Department")
                                       @RequestBody
                                       @Valid DepartmentDTO departmentDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new NotValidRecordException
                    (String.format("Fields of Department have errors: %s", bindingResultParser.getFieldErrMismatches(result)));
        }
        return departmentService.update(departmentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete department")
    public void deleteDepartment(@ApiParam(value = "Id of deleting department (UUID)")
                                 @PathVariable("id") String id) {
        departmentService.deleteById(getUUIDFromString(id));
    }
}
