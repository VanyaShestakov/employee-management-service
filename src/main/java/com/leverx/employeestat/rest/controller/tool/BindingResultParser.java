package com.leverx.employeestat.rest.controller.tool;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class BindingResultParser {

    public String getFieldErrMismatches(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        for (FieldError err : result.getFieldErrors()) {
            sb
                    .append(err.getField()).append(": ")
                    .append(err.getDefaultMessage())
                    .append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
