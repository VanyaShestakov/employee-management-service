package com.leverx.employeestat.rest.exceptionhandler;

import com.leverx.employeestat.rest.exception.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice("com.leverx.employeestat.rest.controller")
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoSuchRecordException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchRecordException(NoSuchRecordException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ExceptionInfo> handleDuplicateRecordException(DuplicateRecordException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityConversionException.class)
    public ResponseEntity<ExceptionInfo> handleEntityConversionException(EntityConversionException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidRecordException.class)
    public ResponseEntity<ExceptionInfo> handleNotValidRecordException(NotValidRecordException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotValidUUIDException.class)
    public ResponseEntity<ExceptionInfo> handleNotValidUUIDException(NotValidUUIDException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionInfo> handleInvalidPasswordException(InvalidPasswordException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(info, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CSVReadingException.class)
    public ResponseEntity<ExceptionInfo> handleCSVReadingException(CSVReadingException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(info, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchReportException.class)
    public ResponseEntity<ExceptionInfo> handleNoSuchReportException(NoSuchReportException exception) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ReportWritingException.class)
    public ResponseEntity<ExceptionInfo> handleReportWritingException(ReportWritingException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AttributesReadingException.class)
    public ResponseEntity<ExceptionInfo> handleAttributesReadingException(AttributesReadingException exception) {
        ExceptionInfo info = new ExceptionInfo(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(info, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
