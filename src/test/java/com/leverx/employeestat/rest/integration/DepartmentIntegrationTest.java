package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.dto.DepartmentDTO;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static com.leverx.employeestat.rest.integration.util.JsonUtils.toJson;
import static com.leverx.employeestat.rest.integration.util.JsonUtils.toObject;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class DepartmentIntegrationTest {

    private static final String DEPARTMENTS_ENDPOINT = "/api/departments";

    private final WebApplicationContext webAppContext;
    private MockMvc mvc;

    @Autowired
    public DepartmentIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnJsonIfGetRequest() throws Exception {
        mvc.perform(get(DEPARTMENTS_ENDPOINT))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCorrectJsonIfGetRequestById() throws Exception {
        mvc.perform(get(DEPARTMENTS_ENDPOINT + "/{id}", "edf939d2-dbc2-4473-80a1-4bb3826e666e"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForGetRequestById() throws Exception {
        mvc.perform(get(DEPARTMENTS_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnNotFoundIfRecordNotFoundById() throws Exception {
        String id = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee";

        mvc.perform(get(DEPARTMENTS_ENDPOINT + "/{id}", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPost() throws Exception {
        mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfNameOfDepartmentAlreadyExistsForPost() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String name = "Department";
        departmentDTO.setName(name);

        mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isCreated());

        mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForPost() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String expected = "Test";
        departmentDTO.setName(expected);

        mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(expected))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void shouldReturnBadRequestIfNameOfDepartmentAlreadyExistsForPut() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String name = "Department";
        departmentDTO.setName(name);

        mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isCreated());

        mvc.perform(put(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForPut() throws Exception {
        mvc.perform(put(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnOkStatusIfJsonWithoutIdIsCorrectForPut() throws Exception{
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String expected = "Test";
        departmentDTO.setName(expected);

        mvc.perform(put(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expected))
                .andExpect(jsonPath("$.id").isNotEmpty());

        assertNull(departmentDTO.getId());
    }

    @Test
    public void shouldReturnUpdatedDepartmentIfItExistsById() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        String name = "Test";
        String expectedName = "Expected";
        departmentDTO.setName(name);

        MvcResult result = mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        departmentDTO = toObject(result.getResponse().getContentAsString(), DepartmentDTO.class);
        departmentDTO.setName(expectedName);
        UUID expectedId = departmentDTO.getId();

        mvc.perform(put(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId.toString()))
                .andExpect(jsonPath("$.name").value(expectedName));
    }

    @Test
    public void shouldDeleteDepartmentIfIdIsCorrectAndExists() throws Exception{
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName("Test");
        MvcResult result = mvc.perform(post(DEPARTMENTS_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(departmentDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        departmentDTO = toObject(result.getResponse().getContentAsString(), DepartmentDTO.class);
        UUID id = departmentDTO.getId();
        mvc.perform(delete(DEPARTMENTS_ENDPOINT + "/{id}", id.toString()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnMethodNotAllowedIfUUIDIsEmptyForDelete() throws Exception {
        mvc.perform(delete(DEPARTMENTS_ENDPOINT + "/{id}", ""))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldReturnBadRequestIfUUIDIsNotCorrectForDeleteRequestById() throws Exception {
        mvc.perform(delete(DEPARTMENTS_ENDPOINT + "/{id}", "incorrect"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.code").value(400));
    }
}
