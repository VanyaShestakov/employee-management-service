package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.entity.*;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.AvailableEmployeeService;
import com.leverx.employeestat.rest.service.ReportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class ReportServiceImpl implements ReportService {

    private final XSSFWorkbook workbook;
    private final XSSFSheet sheet;

    @Value("${reports-path}")
    private String reportsPath;

    private final WorkRepository workRepository;

    @Autowired
    public ReportServiceImpl(WorkRepository workRepository) {
        this.workbook = new XSSFWorkbook();
        this.sheet = (workbook.createSheet("Employees info"));
        this.workRepository = workRepository;
    }

    @Override
    @Transactional
    public void exportOccupationReport(HttpServletResponse response) {
        createTableHeaders("Employee name", "Department", "Project - Occupation");
        List<Work> works = workRepository.findAll();
        for (int i = 0; i < works.size(); i++) {
            Work work = works.get(i);
            Employee employee = work.getId().getEmployee();
            Project project = work.getId().getProject();

            Row row = sheet.createRow(i + 1);

            Cell name = row.createCell(0);
            name.setCellValue(String.format("%s %s", employee.getFirstName(), employee.getLastName()));

            Cell departmentName = row.createCell(1);
            Department department = employee.getDepartment();
            departmentName.setCellValue(department == null ? null : department.getName());

            Cell occupation = row.createCell(2);
            occupation.setCellValue(String.format("%s - (%s - %s)", project.getName(), work.getPositionStartDate(), work.getPositionEndDate()));
        }
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
        } catch (IOException e) {
            System.out.println("AAAA");
        }
    }

    public void exportLastGeneratedReport(HttpServletResponse response) {

    }

    @Scheduled(cron = "0 0 1 * *")
    @Transactional
    public void generateAvailableEmployeesReport() {
        createTableHeaders("First Name", "Last Name", "Department");
        List<Employee> availableEmployees = workRepository.findAllAvailableNext(30)
                .stream()
                .map(Work::getId)
                .map(WorkId::getEmployee)
                .collect(Collectors.toList());

        for (int i = 0; i < availableEmployees.size(); i++) {
            Employee employee = availableEmployees.get(i);
            Row row = sheet.createRow(i + 1);

            Cell firstName = row.createCell(0);
            firstName.setCellValue(employee.getFirstName());

            Cell lastName = row.createCell(1);
            lastName.setCellValue(employee.getLastName());

            Cell department = row.createCell(2);
            department.setCellValue(employee.getDepartment().getName());
        }

        File file = new File(String.format("%s/AvailableEmployees-%s.xlsx", reportsPath, LocalDate.now()));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createTableHeaders(String ... names) {
        Row row = sheet.createRow(0);
        for (int i = 0; i < names.length; i++) {
            sheet.setColumnWidth(i, 12000);
            Cell cell = row.createCell(i);
            cell.setCellValue(names[i]);
        }
    }
}
