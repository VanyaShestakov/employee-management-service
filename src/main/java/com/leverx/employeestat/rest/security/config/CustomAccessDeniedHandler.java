package com.leverx.employeestat.rest.security.config;

import com.leverx.employeestat.rest.exceptionhandler.ExceptionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.leverx.employeestat.rest.util.JsonUtils.toJson;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ExceptionInfo info = new ExceptionInfo(accessDeniedException.getMessage(), HttpStatus.FORBIDDEN);
        response.getOutputStream().println(toJson(info));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("Access denied: " + accessDeniedException.getMessage());
    }
}
