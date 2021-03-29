package com.epam.esm.advice;

import com.epam.esm.advice.error.ResponseError;
import com.epam.esm.exceptions.EntityNotFoundException;
import com.epam.esm.exceptions.EntityWithSuchNameAlreadyExists;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseError> handleEntityNotFoundException() {
        return new ResponseEntity<>(ResponseError.ENTITY_NOT_FOUNT, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityWithSuchNameAlreadyExists.class)
    public ResponseEntity<ResponseError> handleEntityWithSuchNameAlreadyExists() {
        return new ResponseEntity<>(ResponseError.ENTITY_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(ResponseError.INCORRECT_VALIDATION, HttpStatus.BAD_REQUEST);

    }


}
