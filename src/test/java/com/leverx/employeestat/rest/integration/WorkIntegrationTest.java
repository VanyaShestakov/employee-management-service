package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.dto.WorkDTO;
import com.leverx.employeestat.rest.exceptionhandler.GlobalControllerAdvice;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static com.leverx.employeestat.rest.integration.util.JsonUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class WorkIntegrationTest {

    private final static String WORKS_ENDPOINT = "/api/works";
    private final static String IDS_POSTFIX = "/empId={empId}/projId={projId}";
    private final static String NON_EXISTING_ID = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";
    private final WebApplicationContext webAppContext;
    private MockMvc mvc;

    private WorkDTO firstWork;
    private WorkDTO secondWork;
    private WorkDTO thirdWork;

    @Autowired
    public WorkIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @BeforeEach
    public void init() {
        firstWork = new WorkDTO();
        secondWork = new WorkDTO();
        thirdWork = new WorkDTO();

        firstWork.setEmployeeId(UUID.fromString("c7fa1cd3-b281-47b1-b72f-92b9391fc5c7"));
        firstWork.setProjectId(UUID.fromString("7c8697b8-530d-4a00-ba00-3dfec6b1bb79"));

        secondWork.setEmployeeId(UUID.fromString("e9a4e12b-1555-48bd-9275-629856bcdb51"));
        secondWork.setProjectId(UUID.fromString("7c8697b8-530d-4a00-ba00-3dfec6b1bb79"));

        thirdWork.setEmployeeId(UUID.fromString("2174c05c-e885-4521-985d-774b1f05cd37"));
        thirdWork.setProjectId(UUID.fromString("6f6bd93b-fcdd-4c3f-a144-1e03139559d6"));
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnJsonListForGetAllWorks() throws Exception {
        mvc.perform(get(WORKS_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].employeeId").value(firstWork.getEmployeeId().toString()))
                .andExpect(jsonPath("$[1].employeeId").value(secondWork.getEmployeeId().toString()))
                .andExpect(jsonPath("$[2].employeeId").value(thirdWork.getEmployeeId().toString()))
                .andExpect(jsonPath("$[0].projectId").value(firstWork.getProjectId().toString()))
                .andExpect(jsonPath("$[1].projectId").value(secondWork.getProjectId().toString()))
                .andExpect(jsonPath("$[2].projectId").value(thirdWork.getProjectId().toString()));
    }

    @Test
    public void shouldReturnWorkByEmployeeIdAndProjectIdIfTheyAreCorrectForGetWork() throws Exception {
        final String expectedEmpId = firstWork.getEmployeeId().toString();
        final String expectedProjId = firstWork.getProjectId().toString();

        mvc.perform(get(WORKS_ENDPOINT + IDS_POSTFIX, expectedEmpId, expectedProjId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(expectedProjId))
                .andExpect(jsonPath("$.employeeId").value(expectedEmpId));
    }

    @Test
    public void shouldReturnBadRequestIfEmployeeIdAndProjectIdIsNotValidForGetWork() throws Exception {
        final String notValidEmpId = "not valid";
        final String notValidProjId = "not valid";

        mvc.perform(get(WORKS_ENDPOINT + IDS_POSTFIX, notValidEmpId, notValidEmpId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfEmployeeDoesNotExistByEmployeeIdForGetWork() throws Exception {
        final String projId = firstWork.getProjectId().toString();

        mvc.perform(get(WORKS_ENDPOINT + IDS_POSTFIX, NON_EXISTING_ID, projId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Employee with id=%s not found", NON_EXISTING_ID)));
    }

    @Test
    public void shouldReturnNotFoundIfProjectDoesNotExistByProjectIdForGetWork() throws Exception {
        final String empId = firstWork.getEmployeeId().toString();

        mvc.perform(get(WORKS_ENDPOINT + IDS_POSTFIX, empId, NON_EXISTING_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Project with id=%s not found", NON_EXISTING_ID)));
    }

    @Test
    public void shouldReturnNotFoundStatusIfProjectIdAndEmployeeIdExistButWorkIdDoesNotExistForGetWork() throws Exception {
        final String empId = firstWork.getEmployeeId().toString();
        final String projId = thirdWork.getProjectId().toString();

        mvc.perform(get(WORKS_ENDPOINT + IDS_POSTFIX, empId, projId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Work with id=(employeeId=%s; projectId=%s) not found", empId, projId)));
    }

    @Test
    public void shouldReturnOkStatusIfWorkExistsByIdForPutWork() throws Exception {
        final int expectedWorkingHours = 0;
        firstWork.setWorkingHours(expectedWorkingHours);
        mvc.perform(put(WORKS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(firstWork)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workingHours").value(expectedWorkingHours));
    }

    @Test
    public void shouldThrowExceptionIfWorkDoesNotExistByIdForPutWork() throws Exception {
        firstWork.setProjectId(thirdWork.getProjectId());
        mvc.perform(put(WORKS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(firstWork)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Work with id=(employeeId=%s; projectId=%s) not found", firstWork.getEmployeeId(), firstWork.getProjectId())));
    }

    @Test
    public void shouldReturnNoContentStatusIfWorkExistsByIdForDeleteWork() throws Exception {
        final String empId = firstWork.getEmployeeId().toString();
        final String projId = firstWork.getProjectId().toString();

        mvc.perform(delete(WORKS_ENDPOINT + IDS_POSTFIX, empId, projId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundStatusIfProjectIdAndEmployeeIdExistButWorkIdDoesNotExistForDeleteWork() throws Exception {
        final String empId = firstWork.getEmployeeId().toString();
        final String projId = thirdWork.getProjectId().toString();

        mvc.perform(delete(WORKS_ENDPOINT + IDS_POSTFIX, empId, projId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Work with id=(employeeId=%s; projectId=%s) not found", empId, projId)));
    }
}
