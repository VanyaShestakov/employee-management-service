package com.leverx.employeestat.rest.exception;

public class DuplicateProjectException extends RuntimeException {

    public DuplicateProjectException() {
        super();
    }

    public DuplicateProjectException(String message) {
        super(message);
    }

    public DuplicateProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateProjectException(Throwable cause) {
        super(cause);
    }

    protected DuplicateProjectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
