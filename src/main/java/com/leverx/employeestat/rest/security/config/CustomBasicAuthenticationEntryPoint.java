package com.leverx.employeestat.rest.security.config;

import com.leverx.employeestat.rest.exceptionhandler.ExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.employeestat.rest.util.JsonUtils.toJson;

@Component
@Slf4j
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ExceptionInfo info = new ExceptionInfo(authException.getMessage(), HttpStatus.UNAUTHORIZED);
        response.getOutputStream().println(toJson(info));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("Authentication failed: " + authException.getMessage());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("Access to secured endpoints");
        super.afterPropertiesSet();
    }
}
