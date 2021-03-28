package com.epam.esm.advice;

import com.epam.esm.advice.error.ResponseError;
import com.epam.esm.exceptions.EntityNotFoundException;
import com.epam.esm.exceptions.EntityWithSuchNameAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final static int ENTITY_NOT_FOUND_CODE = 40401;
    private final static int ENTITY_ALREADY_EXISTS_CODE = 42000;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handleEntityNotFoundException(EntityNotFoundException exception) {
        ResponseError error = new ResponseError(exception.getLocalizedMessage(), ENTITY_NOT_FOUND_CODE);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityWithSuchNameAlreadyExists.class)
    public ResponseEntity<ResponseError> handleEntityWithSuchNameAlreadyExists(EntityWithSuchNameAlreadyExists exception) {
        ResponseError error = new ResponseError(exception.getLocalizedMessage(), ENTITY_ALREADY_EXISTS_CODE);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }


}
