package com.leverx.employeestat.rest.service;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    void export(HttpServletResponse response);
}
