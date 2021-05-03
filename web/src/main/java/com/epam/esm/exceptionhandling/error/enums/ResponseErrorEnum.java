package com.epam.esm.exceptionhandling.error.enums;

import com.epam.esm.constants.ApplicationConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonIgnoreProperties(value = {"status"})
public enum ResponseErrorEnum {
    GIFT_CERTIFICATE_NOT_FOUND("Gift certificate is not found", HttpStatus.NOT_FOUND, ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE),
    GIFT_CERTIFICATE_WITH_SUCH_NAME_EXISTS("Gift certificate already exists", HttpStatus.BAD_REQUEST, ApplicationConstants.CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE),

    TAG_NOT_FOUND("Tag is not found", HttpStatus.NOT_FOUND, ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE),
    TAG_WITH_SUCH_NAME_EXISTS("Tag already exists", HttpStatus.BAD_REQUEST, ApplicationConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE),

    USER_NOT_FOUND("User is not found", HttpStatus.NOT_FOUND, ApplicationConstants.USER_NOT_FOUND_ERROR_CODE),
    USER_WITH_SUCH_LOGIN_EXISTS("User already exists", HttpStatus.BAD_REQUEST, ApplicationConstants.USER_SUCH_LOGIN_EXISTS_CODE),

    ORDER_NOT_FOUND("Order is not found", HttpStatus.NOT_FOUND, ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE),
    INCORRECT_OPERATOR_VALUE_PASSED_IN_REQUEST("Incorrect operator value was passed in request parameter", HttpStatus.BAD_REQUEST, ApplicationConstants.INCORRECT_OPERATOR_VALUE);

    private final String message;
    private final int code;
    private final HttpStatus status;

    ResponseErrorEnum(String message, HttpStatus status, int code) {
        this.message = message;
        this.code = code;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
