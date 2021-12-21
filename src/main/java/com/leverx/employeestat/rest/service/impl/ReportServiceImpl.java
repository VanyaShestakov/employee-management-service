package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.entity.*;
import com.leverx.employeestat.rest.exception.AttributesReadingException;
import com.leverx.employeestat.rest.exception.NoSuchReportException;
import com.leverx.employeestat.rest.exception.ReportWritingException;
import com.leverx.employeestat.rest.repository.WorkRepository;
import com.leverx.employeestat.rest.service.ReportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final Logger log = LogManager.getLogger(ReportServiceImpl.class);

    private final static String FIRST_NAME_COL = "First Name";
    private final static String LAST_NAME_COL = "Last Name";
    private final static String DEPARTMENT_COL = "Department";
    private final static String PROJECT_OCCUPATION_COL = "Project - Occupation";

    @Value("${reports-path}")
    private String reportsPath;
    private XSSFWorkbook workbook;
    private final XSSFSheet sheet;
    private final WorkRepository workRepository;

    @Autowired
    public ReportServiceImpl(WorkRepository workRepository) {
        this.workbook = new XSSFWorkbook();
        this.sheet = (workbook.createSheet("Employees info"));
        this.workRepository = workRepository;
    }

    @Override
    @Scheduled(cron = "${reports-generating-cron}")
    @Transactional
    public void generateAvailableEmployeesReport() {
        createTableHeaders(FIRST_NAME_COL, LAST_NAME_COL, DEPARTMENT_COL);
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
            throw new ReportWritingException("Failed to write report", e);
        }
        log.info("Report generated successfully (every month report)");
    }

    @Override
    @Transactional
    public XSSFWorkbook exportOccupationReport() {
        createTableHeaders(FIRST_NAME_COL, LAST_NAME_COL, DEPARTMENT_COL, PROJECT_OCCUPATION_COL);
        List<Work> works = workRepository.findAll();
        for (int i = 0; i < works.size(); i++) {
            Work work = works.get(i);
            Employee employee = work.getId().getEmployee();
            Project project = work.getId().getProject();

            Row row = sheet.createRow(i + 1);

            Cell firstName = row.createCell(0);
            firstName.setCellValue(employee.getFirstName());

            Cell lastName = row.createCell(1);
            lastName.setCellValue(employee.getLastName());

            Cell departmentName = row.createCell(2);
            Department department = employee.getDepartment();
            departmentName.setCellValue(department == null ? null : department.getName());

            Cell occupation = row.createCell(3);
            occupation.setCellValue(String.format("%s - (%s - %s)", project.getName(), work.getPositionStartDate(), work.getPositionEndDate()));
        }
        log.info("Occupation report created successfully");
        return workbook;
    }

    @Override
    public XSSFWorkbook exportLastGeneratedReport() {
        File latestReport = getLatestReport();
        log.info("Latest report retrieved successfully");
        try {
            return new XSSFWorkbook(latestReport);
        } catch (IOException | InvalidFormatException e) {
            throw new ReportWritingException("Failed to write report", e);
        }
    }

    private File getLatestReport() {
        File[] reports = new File(reportsPath).listFiles();
        return Arrays.stream(reports)
                .sorted((f1, f2) -> {
                    BasicFileAttributes attr1 = null;
                    BasicFileAttributes attr2 = null;
                    try {
                        attr1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
                        attr2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);
                    } catch (IOException e) {
                        throw new AttributesReadingException("Failed to read file attributes", e);
                    }
                    return (-1) * attr1.creationTime().compareTo(attr2.creationTime());
                })
                .findFirst()
                .orElseThrow(() -> {
                    throw new NoSuchReportException("No reports found");
        });
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
