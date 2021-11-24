package com.leverx.employeestat.rest.exception;

public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException() {
        super();
    }

    public DuplicateEmployeeException(String message) {
        super(message);
    }

    public DuplicateEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmployeeException(Throwable cause) {
        super(cause);
    }

    protected DuplicateEmployeeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
