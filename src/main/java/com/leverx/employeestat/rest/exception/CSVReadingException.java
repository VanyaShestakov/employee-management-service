package com.leverx.employeestat.rest.exception;

public class CSVReadingException extends RuntimeException {

    public CSVReadingException() {
        super();
    }

    public CSVReadingException(String message) {
        super(message);
    }

    public CSVReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
