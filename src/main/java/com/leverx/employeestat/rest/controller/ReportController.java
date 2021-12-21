package com.leverx.employeestat.rest.controller;

import com.leverx.employeestat.rest.exception.ReportWritingException;
import com.leverx.employeestat.rest.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("api/employees/export")
@Api(tags = "API for exporting workload reports of employees")
public class ReportController {

    private final Logger log = LogManager.getLogger(ReportController.class);

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Export occupation-report of employees")
    public void exportEmployees(HttpServletResponse response) {
        log.info("executing exportEmployees() method");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employees.xlsx");
        XSSFWorkbook workbook = reportService.exportOccupationReport();
        writeToResponse(workbook, response);
    }

    @GetMapping("/last")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Export last generated report of available employees for the next month")
    public void exportLastGeneratedReport(HttpServletResponse response) {
        log.info("executing exportLastGeneratedReport() method");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Employees-Last.xlsx");
        XSSFWorkbook workbook = reportService.exportLastGeneratedReport();
        writeToResponse(workbook, response);
    }

    private void writeToResponse(XSSFWorkbook workbook, HttpServletResponse response) {
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new ReportWritingException("Failed to write report", e);
        }
    }
}



