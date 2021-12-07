package com.leverx.employeestat.rest.exception;

public class NoSuchReportException extends RuntimeException{
    public NoSuchReportException() {
    }

    public NoSuchReportException(String message) {
        super(message);
    }

    public NoSuchReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
