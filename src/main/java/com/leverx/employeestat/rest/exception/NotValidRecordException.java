package com.leverx.employeestat.rest.exception;

public class NotValidRecordException extends RuntimeException {

    public NotValidRecordException() {
        super();
    }

    public NotValidRecordException(String message) {
        super(message);
    }

    public NotValidRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
