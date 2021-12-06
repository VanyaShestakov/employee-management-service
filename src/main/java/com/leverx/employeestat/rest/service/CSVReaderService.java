package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CSVReaderService {

    List<EmployeeDTO> getEmployeesFromFile(MultipartFile file);
}
