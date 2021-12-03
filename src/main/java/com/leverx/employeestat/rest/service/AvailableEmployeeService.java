package com.leverx.employeestat.rest.service;

import com.leverx.employeestat.rest.dto.EmployeeDTO;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.apache.batik.svggen.font.table.LigatureSet;

import java.util.List;

public interface AvailableEmployeeService {
    
    List<EmployeeDTO> getAvailableEmployeesNow();

    List<EmployeeDTO> getAvailableEmployeesNext(int days);
}
