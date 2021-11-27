package com.leverx.employeestat.rest.exception;

public class DuplicateDepartmentException extends DuplicateRecordException {

    public DuplicateDepartmentException() {
        super();
    }

    public DuplicateDepartmentException(String message) {
        super(message);
    }

    public DuplicateDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateDepartmentException(Throwable cause) {
        super(cause);
    }

    protected DuplicateDepartmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
