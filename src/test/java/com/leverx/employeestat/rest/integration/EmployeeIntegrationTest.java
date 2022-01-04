package com.leverx.employeestat.rest.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.dto.EmployeeDTO;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class EmployeeIntegrationTest {

    public static final String EMPLOYEES_ENDPOINT = "/api/employees";

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;
    private EmployeeDTO employeeDTO;

    @Autowired
    public EmployeeIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

    @BeforeEach
    public void init() {
        employeeDTO = new EmployeeDTO();
        String id = "c7fa1cd3-b281-47b1-b72f-92b9391fc5c7";
        String expectedFirstName = "Ivan";
        String expectedLastName = "Shestakov";
        String expectedUsername = "Vanechka";
        String expectedPosition = "Java Developer";
        String role = "ROLE_MANAGER";
        employeeDTO.setFirstName(expectedFirstName);
        employeeDTO.setLastName(expectedLastName);
        employeeDTO.setUsername(expectedUsername);
        employeeDTO.setPosition(expectedPosition);
        employeeDTO.setRole(role);
        employeeDTO.setId(UUID.fromString(id));
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnJsonIfGetRequest() throws Exception {
        mvc.perform(get(EMPLOYEES_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectJsonIfGetRequestById() throws Exception {
        mvc.perform(get(EMPLOYEES_ENDPOINT + "/{id}", employeeDTO.getId().toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(employeeDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employeeDTO.getLastName()))
                .andExpect(jsonPath("$.username").value(employeeDTO.getUsername()))
                .andExpect(jsonPath("$.position").value(employeeDTO.getPosition()));
    }

    @Test
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForGetRequestById() throws Exception {
        mvc.perform(get(EMPLOYEES_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnMethodNotAllowedForPost() throws Exception {
        mvc.perform(post(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsEmptyForPut() throws Exception {
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfJsonHasEmptyFieldsForPut() throws Exception {
        employeeDTO.setPosition("");
        employeeDTO.setUsername("");
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundIfRoleDoesNotExistForPut() throws Exception {
        employeeDTO.setRole("Test");
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForPut() throws Exception {
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(employeeDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(employeeDTO.getLastName()))
                .andExpect(jsonPath("$.username").value(employeeDTO.getUsername()))
                .andExpect(jsonPath("$.position").value(employeeDTO.getPosition()))
                .andExpect(jsonPath("$.id").value(employeeDTO.getId().toString()));
    }

    @Test
    public void shouldReturnUpdatedEmployeeIfItExistsById() throws Exception {
        mvc.perform(put(EMPLOYEES_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeDTO.getId().toString()))
                .andExpect(jsonPath("$.firstName").value(employeeDTO.getFirstName()));
    }

    @Test
    public void shouldDeleteEmployeeIfIdIsCorrectAndExists() throws Exception{
        mvc.perform(delete(EMPLOYEES_ENDPOINT + "/{id}", "c7fa1cd3-b281-47b1-b72f-92b9391fc5c7"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnMethodNotAllowedIfUUIDIsEmptyForDelete() throws Exception {
        mvc.perform(delete(EMPLOYEES_ENDPOINT + "/{id}", ""))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForDeleteRequestById() throws Exception {
        mvc.perform(delete(EMPLOYEES_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(object);
    }

    private EmployeeDTO toObject(String jsonString) throws JsonProcessingException {
        return new ObjectMapper().readValue(jsonString, EmployeeDTO.class);
    }
}
