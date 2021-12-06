package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.Employee;
import com.leverx.employeestat.rest.entity.Work;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.ReportService;
import com.leverx.employeestat.rest.service.WorkService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final WorkRepository workRepository;

    @Autowired
    public ReportServiceImpl(WorkRepository workRepository) {
        this.workbook = new XSSFWorkbook();
        this.sheet = (workbook.createSheet("Employees info"));
        this.workRepository = workRepository;
    }

    @Override
    public void export(HttpServletResponse response) {
        createHeaders();
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } catch (IOException e) {
            System.out.println("AAAA");
        }
    }


    private void fillTable() {
        List<Work> works = workRepository.findAll();
        for (int i = 0; i < works.size(); i++) {
            Employee employee = works.get(i).getId().getEmployee();
            Row row = sheet.createRow(i+1);
            Cell name = row.createCell(0);
            name.setCellValue(String.format("%s %s", employee.getFirstName(), employee.getLastName()));

            Cell department = row.createCell(1);
            department.setCellValue(employee.getDepartment().getName());

            Cell occupation = row.createCell(2);
            occupation.setCellValue("Project - occupation");
        }
    }

    private void createHeaders() {
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 10000);
        sheet.setColumnWidth(2, 20000);
        Row row = sheet.createRow(0);

        Cell name = row.createCell(0);
        name.setCellValue("Employee name");

        Cell department = row.createCell(1);
        department.setCellValue("Department");

        Cell occupation = row.createCell(2);
        occupation.setCellValue("Project - occupation");
    }
}
