package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.leverx.employeestat.rest.controller.tool.UUIDUtils.getUUIDFromString;

@RestController
@RequestMapping("/api/works")
public class WorkController {

    private final WorkService workService;

    @Autowired
    public WorkController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<WorkDTO> getWorks() {
        return workService.getAll();
    }

    @GetMapping("/empId={empId}/projId={projId}")
    @ResponseStatus(HttpStatus.OK)
    public WorkDTO getWork(@PathVariable("empId") UUID employeeId, @PathVariable("projId") UUID projectId) {
        return workService.getByIds(employeeId, projectId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkDTO putWork(@RequestBody WorkDTO workDTO) {
        return workService.update(workDTO);
    }

    @DeleteMapping("/empId={empId}/projId={projId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteWork(@PathVariable("empId") String employeeId, @PathVariable("projId") String projectId) {
        workService.deleteByIds(getUUIDFromString(employeeId), getUUIDFromString(projectId));
    }
}
