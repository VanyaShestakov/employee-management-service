package com.leverx.employeestat.rest.exception;

public class NotValidUUIDException extends RuntimeException {
    public NotValidUUIDException() {
        super();
    }

    public NotValidUUIDException(String message) {
        super(message);
    }

    public NotValidUUIDException(String message, Throwable cause) {
        super(message, cause);
    }
}
