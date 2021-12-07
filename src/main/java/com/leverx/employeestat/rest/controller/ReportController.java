package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/employees/export")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void exportEmployees(HttpServletResponse response) {
        reportService.exportOccupationReport(response);
    }

    @GetMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    public void exportLastGeneratedReport(HttpServletResponse response) {
        reportService.exportLastGeneratedReport(response);
    }
}



