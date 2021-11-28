package com.leverx.employeestat.rest.exceptionhandler;

import com.leverx.employeestat.rest.controller.DepartmentController;
import com.leverx.employeestat.rest.exception.NoSuchRecordException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice("com.leverx.employeestat.rest.controller")
public class DepartmentControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchRecordException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchRecordException(NoSuchRecordException exception) {
        ExceptionInfo info = new ExceptionInfo();
        info.setStatus(HttpStatus.NOT_FOUND);
        info.setCode(HttpStatus.NOT_FOUND.value());
        info.setMessage(exception.getMessage());
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

}
