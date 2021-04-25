package com.epam.esm.advice;

import com.epam.esm.advice.error.ResponseError;
import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dto.result.ErrorResult;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.exceptions.OrderException;
import com.epam.esm.exceptions.ResponseErrorNotFoundException;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.exceptions.UserException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GiftCertificateException.class)
    public ResponseEntity<ResponseError> handleGiftCertificateException(GiftCertificateException exception) {
        int errorCode = exception.getErrorCode();
        ResponseError error = getError(errorCode);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(TagException.class)
    public ResponseEntity<ResponseError> handleTagException(TagException exception) {
        int errorCode = exception.getErrorCode();
        ResponseError error = getError(errorCode);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ResponseError> handleOrderException(OrderException exception) {
        int errorCode = exception.getErrorCode();
        ResponseError error = getError(errorCode);
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseError> handleUserException(UserException exception) {
        int errorCode = exception.getErrorCode();
        ResponseError error = getError(errorCode);
        return new ResponseEntity<>(error, error.getStatus());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        List<String> messages = constraintViolations.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ErrorResult(ApplicationConstants.DEFAULT_VALIDATION_ERROR_CODE, messages), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<ObjectError> errors = exception.getBindingResult().getAllErrors();

        List<String> messages = errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResult(ApplicationConstants.DEFAULT_VALIDATION_ERROR_CODE, messages), HttpStatus.BAD_REQUEST);
    }


    private ResponseError getError(int errorCode) {
        return Stream.of(ResponseError.values())
                .filter(error -> error.getCode() == errorCode)
                .findFirst().orElseThrow(() -> new ResponseErrorNotFoundException(String.format("Can't find ResponseError with code:%d", errorCode)));
    }


}
