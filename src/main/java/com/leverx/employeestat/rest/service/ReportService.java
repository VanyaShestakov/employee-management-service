package com.leverx.employeestat.rest.service;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    void exportOccupationReport(HttpServletResponse response);

    void exportLastGeneratedReport(HttpServletResponse response);

    void generateAvailableEmployeesReport();
}
