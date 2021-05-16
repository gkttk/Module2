package com.epam.esm.exceptionhandling;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.exceptionhandling.error.ErrorResult;
import com.epam.esm.exceptionhandling.error.enums.ResponseErrorEnum;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.exceptions.OrderException;
import com.epam.esm.exceptions.ResponseErrorNotFoundException;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ExceptionControllerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ResponseEntity<ErrorResult> getResponseEntity(int errorCode, Locale locale) {
        ResponseErrorEnum error = getError(errorCode);
        String message = messageSource.getMessage(error.getPropertyKey(), null, locale);

        return new ResponseEntity<>(new ErrorResult(errorCode, Collections.singletonList(message)), error.getStatus());
    }

    @ExceptionHandler(GiftCertificateException.class)
    public ResponseEntity<ErrorResult> handleGiftCertificateException(GiftCertificateException exception, Locale locale) {
        return getResponseEntity(exception.getErrorCode(), locale);
    }

    @ExceptionHandler(TagException.class)
    public ResponseEntity<ErrorResult> handleTagException(TagException exception, Locale locale) {
        return getResponseEntity(exception.getErrorCode(), locale);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResult> handleOrderException(OrderException exception, Locale locale) {
        return getResponseEntity(exception.getErrorCode(), locale);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResult> handleUserException(UserException exception, Locale locale) {
        return getResponseEntity(exception.getErrorCode(), locale);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> handleConstraintViolationException(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        List<String> messages = constraintViolations.stream()
                .map(ConstraintViolation::getMessageTemplate)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ErrorResult(WebLayerConstants.DEFAULT_VALIDATION_ERROR_CODE, messages), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<ObjectError> errors = exception.getBindingResult().getAllErrors();

        List<String> messages = errors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ErrorResult(WebLayerConstants.DEFAULT_VALIDATION_ERROR_CODE, messages), HttpStatus.BAD_REQUEST);
    }


    private ResponseErrorEnum getError(int errorCode) {
        return Stream.of(ResponseErrorEnum.values())
                .filter(error -> error.getCode() == errorCode)
                .findFirst().orElseThrow(() -> new ResponseErrorNotFoundException(String.format("Can't find ResponseError with code:%d", errorCode)));
    }


}
