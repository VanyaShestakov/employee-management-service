package com.leverx.employeestat.rest.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ReportService {

    XSSFWorkbook exportOccupationReport();

    XSSFWorkbook exportLastGeneratedReport();

    void generateAvailableEmployeesReport();
}
