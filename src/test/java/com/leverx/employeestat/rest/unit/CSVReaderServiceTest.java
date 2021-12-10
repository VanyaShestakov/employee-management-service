package com.leverx.employeestat.rest.unit;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import com.leverx.employeestat.rest.service.CSVReaderService;
import com.leverx.employeestat.rest.service.impl.CSVReaderServiceImpl;
import com.leverx.employeestat.rest.unit.fileitems.FileItemImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReaderServiceTest {

    private final static String CORRECT_FILE = "csv-test/employees.csv";

    private CSVReaderService csvReaderService;

    @BeforeEach
    public void init() {
        csvReaderService = new CSVReaderServiceImpl();
    }

    @Test
    public void shouldReturnNotEmptyListIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);
        Assertions.assertFalse(employees.isEmpty());
    }

    @Test
    public void shouldReturnCorrectEmployeesAmountIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);

        int expectedAmount = 5;
        int actualAmount = employees.size();

        Assertions.assertEquals(expectedAmount, actualAmount);
    }

    @Test
    public void shouldReturnCorrectFirstNamesIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);

        List<String> expectedNames = Arrays.asList(new String[] { "Vlad", "Artiom", "Kirill", "Aleksander", "Dasha"});
        List<String> actualNames = employees
                .stream()
                .map(EmployeeDTO::getFirstName)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedNames, actualNames);
    }

    @Test
    public void shouldReturnCorrectLastNamesIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);

        List<String> expectedLastNames = Arrays.asList(new String[] {"Vladov", "Ivanov", "Avtobusov", "Sosiskin", "Glavnaya"});
        List<String> actualLastNames = employees
                .stream()
                .map(EmployeeDTO::getLastName)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedLastNames, actualLastNames);
    }

    @Test
    public void shouldReturnCorrectUsernamesIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);

        List<String> expectedUsernames = Arrays.asList(new String[] {"Turbo3000", "Tema", "Kiruha", "Sanek1000", "Dashka"});
        List<String> actualUsernames = employees
                .stream()
                .map(EmployeeDTO::getUsername)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedUsernames, actualUsernames);
    }

    @Test
    public void shouldReturnCorrectPositionIfFileIsCorrect() {
        MultipartFile file = new CommonsMultipartFile(new FileItemImpl(CORRECT_FILE));
        List<EmployeeDTO> employees = csvReaderService.getEmployeesFromFile(file);

        List<String> expectedPositions = Arrays.asList(new String[] {"Java Developer", "JS Developer", ".NET Developer", "HR", "Boss"});
        List<String> actualPositions = employees
                .stream()
                .map(EmployeeDTO::getPosition)
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedPositions, actualPositions);
    }
}
