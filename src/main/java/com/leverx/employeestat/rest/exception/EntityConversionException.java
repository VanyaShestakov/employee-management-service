package com.leverx.employeestat.rest.exception;

public class EntityConversionException extends RuntimeException{
    public EntityConversionException() {
        super();
    }

    public EntityConversionException(String message) {
        super(message);
    }

    public EntityConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityConversionException(Throwable cause) {
        super(cause);
    }

    protected EntityConversionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
