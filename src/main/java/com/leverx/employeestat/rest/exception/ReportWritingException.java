package com.leverx.employeestat.rest.exception;

public class ReportWritingException extends RuntimeException {

    public ReportWritingException() {
        super();
    }

    public ReportWritingException(String message) {
        super(message);
    }

    public ReportWritingException(String message, Throwable cause) {
        super(message, cause);
    }
}
