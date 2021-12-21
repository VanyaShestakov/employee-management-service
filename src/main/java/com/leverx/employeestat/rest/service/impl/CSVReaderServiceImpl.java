package com.leverx.employeestat.rest.service.impl;

import com.leverx.employeestat.rest.controller.AvailableEmployeeController;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.exception.CSVReadingException;
import com.leverx.employeestat.rest.service.CSVReaderService;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CSVReaderServiceImpl implements CSVReaderService {

    private final Logger log = LogManager.getLogger(CSVReaderServiceImpl.class);

    private final static int FIRST_NAME_COLUMN = 0;
    private final static int LAST_NAME_COLUMN = 1;
    private final static int USERNAME_COLUMN = 2;
    private final static int PASSWORD_COLUMN = 3;
    private final static int POSITION_COLUMN = 4;
    private final static int ROLE_COLUMN = 5;
    private final static int DEPARTMENT_ID_COLUMN = 6;

    public List<EmployeeDTO> getEmployeesFromFile(MultipartFile file) {
        List<EmployeeDTO> employees = new ArrayList<>();
        try(CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] cells;
            while ((cells = reader.readNext()) != null) {
                EmployeeDTO employee = new EmployeeDTO();
                employee.setFirstName(cells[FIRST_NAME_COLUMN]);
                employee.setLastName(cells[LAST_NAME_COLUMN]);
                employee.setUsername(cells[USERNAME_COLUMN]);
                employee.setPassword(cells[PASSWORD_COLUMN]);
                employee.setPosition(cells[POSITION_COLUMN]);
                employee.setRole(cells[ROLE_COLUMN]);
                employee.setDepartmentId(UUID.fromString(cells[DEPARTMENT_ID_COLUMN]));
                employees.add(employee);
            }
        } catch (IOException | CsvValidationException e) {
            CSVReadingException ex = new CSVReadingException(e.getMessage(), e);
            log.error("Thrown exception", ex);
            throw ex;
        }
        log.info("Multipart file (CSV) read successfully");
        return employees;
    }
}
