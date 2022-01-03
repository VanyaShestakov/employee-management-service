package com.leverx.employeestat.rest.exception;

public class EntityConversionException extends RuntimeException {

    public EntityConversionException() {
        super();
    }

    public EntityConversionException(String message) {
        super(message);
    }

    public EntityConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
