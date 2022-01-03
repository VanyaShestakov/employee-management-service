package com.leverx.employeestat.rest.exception;

public class AttributesReadingException extends RuntimeException {

    public AttributesReadingException() {
        super();
    }

    public AttributesReadingException(String message) {
        super(message);
    }

    public AttributesReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
