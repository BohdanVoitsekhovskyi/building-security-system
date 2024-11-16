package com.building_security_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DetectorNotFoundException.class)
    public ResponseEntity<ErrorObject> handleDetectorNotFoundException(
            DetectorNotFoundException ex,
            WebRequest request
    ) {
        return buildErrorObject(ex, request);
    }

    @ExceptionHandler(FacilityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleFacilityNotFoundException(
            DetectorNotFoundException ex,
            WebRequest request
    ) {
        return buildErrorObject(ex, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(
            DetectorNotFoundException ex,
            WebRequest request
    ) {
        return buildErrorObject(ex, request);
    }

    private ResponseEntity<ErrorObject> buildErrorObject(
            DetectorNotFoundException ex,
            WebRequest request
    ) {
        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
}