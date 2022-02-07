package com.leverx.employeestat.rest.integration;

import com.leverx.employeestat.rest.configuration.WebInitializer;
import com.leverx.employeestat.rest.exceptionhandler.GlobalControllerAdvice;
import com.leverx.employeestat.rest.integration.config.TestConfig;
import com.leverx.employeestat.rest.model.RegistrationRequest;
import com.leverx.employeestat.rest.model.ResetPasswordRequest;
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

import static com.leverx.employeestat.rest.integration.util.JsonUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class, WebInitializer.class})
@WebAppConfiguration
@Transactional
public class AuthorizationIntegrationTest {

    private static final String REGISTER_ENDPOINT = "/api/register";
    private static final String RESET_ENDPOINT = "/api/reset-password";

    private final WebApplicationContext webAppContext;

    private MockMvc mvc;

    private RegistrationRequest registrationRequest;
    private ResetPasswordRequest resetPasswordRequest;

    @Autowired
    public AuthorizationIntegrationTest(WebApplicationContext webAppContext) {
        this.webAppContext = webAppContext;
    }

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @BeforeEach
    public void init() {
        registrationRequest = new RegistrationRequest();
        resetPasswordRequest = new ResetPasswordRequest();

        registrationRequest.setFirstName("name");
        registrationRequest.setLastName("last name");
        registrationRequest.setPassword("init");
        registrationRequest.setPosition("position");
        registrationRequest.setUsername("username");
        registrationRequest.setRole("ROLE_EMPLOYEE");

        resetPasswordRequest.setOldPassword("init");
        resetPasswordRequest.setNewPassword("new");
        resetPasswordRequest.setUsername("username");
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalControllerAdvice advice = this.webAppContext.getBean(GlobalControllerAdvice.class);
        assertNotNull(advice);
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectForRegisterEmployee() throws Exception {
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonFieldsIsNotValidForRegisterEmployee() throws Exception {
        registrationRequest.setUsername("");
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonContainsExistingUsernameForRegisterEmployee() throws Exception {
        registrationRequest.setUsername("Vanechka");
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format("Employee with username=%s already exists", registrationRequest.getUsername())));
    }

    @Test
    public void shouldReturnBadRequestStatusIfJsonIsNotCorrectForRegisterEmployee() throws Exception {
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundStatusIfJsonContainsNotExistingRoleForRegisterEmployee() throws Exception {
        registrationRequest.setRole("not existing");
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Role with name=%s does not exists", registrationRequest.getRole())));
    }

    @Test
    public void shouldReturnOkStatusIfJsonIsCorrectAndContainsIsValidOldPasswordForResetPassword() throws Exception {
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isCreated());

        mvc.perform(post(RESET_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(resetPasswordRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundStatusIfEmployeeDoesNotExistByUsernameForResetPassword() throws Exception {
        mvc.perform(post(RESET_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(resetPasswordRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("Employee with username=%s not found", resetPasswordRequest.getUsername())));
    }

    @Test
    public void shouldReturnUnauthorizedStatusIfOldPasswordMismatchesWithCurrentForResetPassword() throws Exception {
        mvc.perform(post(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(registrationRequest)))
                .andExpect(status().isCreated());
        resetPasswordRequest.setOldPassword("incorrect");
        mvc.perform(post(RESET_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(toJson(resetPasswordRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("You entered incorrect old password"));
    }
}
