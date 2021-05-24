package com.epam.esm.exceptionhandling;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.domain.exceptions.GiftApplicationException;
import com.epam.esm.domain.exceptions.GiftCertificateException;
import com.epam.esm.domain.exceptions.OrderException;
import com.epam.esm.domain.exceptions.ResponseErrorNotFoundException;
import com.epam.esm.domain.exceptions.TagException;
import com.epam.esm.domain.exceptions.UserException;
import com.epam.esm.exceptionhandling.error.ErrorResult;
import com.epam.esm.exceptionhandling.error.enums.ResponseErrorEnum;
import com.epam.esm.security.exceptions.GiftApplicationAccessDeniedException;
import com.epam.esm.security.exceptions.GiftApplicationUnauthorizedException;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import org.springframework.beans.TypeMismatchException;
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

    private ResponseEntity<ErrorResult> getResponseEntity(int errorCode, Object[] params, Locale locale) {
        ResponseErrorEnum error = getError(errorCode);
        String message = messageSource.getMessage(error.getPropertyKey(), null, locale);

        return new ResponseEntity<>(new ErrorResult(errorCode, Collections.singletonList(String.format(message, params))), error.getStatus());
    }

    @ExceptionHandler(value = {GiftCertificateException.class, TagException.class, OrderException.class, UserException.class,
            JwtAuthenticationException.class, GiftApplicationAccessDeniedException.class, GiftApplicationUnauthorizedException.class})
    public ResponseEntity<ErrorResult> handleGiftCertificateException(GiftApplicationException exception, Locale locale) {
        return getResponseEntity(exception.getErrorCode(), exception.getParams(), locale);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult> handleConstraintViolationException(ConstraintViolationException exception, Locale locale) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        List<String> messages = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .map(messageTemplate -> messageSource.getMessage(messageTemplate, null, locale))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ErrorResult(WebLayerConstants.DEFAULT_VALIDATION_ERROR_CODE, messages), HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Object[] params = new Object[]{ex.getValue(), ex.getRequiredType()};
        ResponseErrorEnum error = getError(WebLayerConstants.MISMATCH_PARAMETER_ERROR_CODE);
        String message = messageSource.getMessage(error.getPropertyKey(), null, request.getLocale());

        return new ResponseEntity<>(new ErrorResult(error.getCode(), Collections.singletonList(String.format(message, params))), error.getStatus());
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
