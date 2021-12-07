package com.leverx.employeestat.rest.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ReportService {

    XSSFWorkbook exportOccupationReport();

    XSSFWorkbook exportLastGeneratedReport();

    void generateAvailableEmployeesReport();
}
