package com.epam.esm.advice;

import com.epam.esm.advice.error.ResponseError;
import com.epam.esm.exceptions.*;
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

    @ExceptionHandler(GiftCertificateNotFoundException.class)
    public ResponseEntity<ResponseError> handleGiftCertificateNotFoundException() {
        return new ResponseEntity<>(ResponseError.GIFT_CERTIFICATE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ResponseError> handleTagNotFoundException() {
        return new ResponseEntity<>(ResponseError.TAG_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagWithSuchNameAlreadyExists.class)
    public ResponseEntity<ResponseError> handleEntityWithSuchNameAlreadyExists() {
        return new ResponseEntity<>(ResponseError.TAG_WITH_SUCH_NAME_EXISTS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GiftCertificateWithSuchNameAlreadyExists.class)
    public ResponseEntity<ResponseError> handleGiftCertificateWithSuchNameAlreadyExists() {
        return new ResponseEntity<>(ResponseError.GIFT_CERTIFICATE_WITH_SUCH_NAME_EXISTS, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IllegalRequestParameterException.class)
    public ResponseEntity<ResponseError> handleIllegalRequestParameterException() {
        return new ResponseEntity<>(ResponseError.ILLEGAL_REQUEST_PARAMETER, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectSortingFieldException.class)
    public ResponseEntity<ResponseError> handleIncorrectSortingFieldException() {
        return new ResponseEntity<>(ResponseError.INCORRECT_SORTING_FIELD, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(ResponseError.INCORRECT_VALIDATION, HttpStatus.BAD_REQUEST);

    }


}
