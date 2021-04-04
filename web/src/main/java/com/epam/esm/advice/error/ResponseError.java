package com.epam.esm.advice.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseError {
    GIFT_CERTIFICATE_NOT_FOUND("Gift certificate is not found", 40401),
    TAG_NOT_FOUND("Tag is not found", 40402),
    TAG_WITH_SUCH_NAME_EXISTS("Tag already exists", 42000),
    GIFT_CERTIFICATE_WITH_SUCH_NAME_EXISTS("Gift certificate already exists", 42010),
    INCORRECT_VALIDATION("Incorrect validation", 43501),
    ILLEGAL_REQUEST_PARAMETER("Illegal request parameter", 44600),
    INCORRECT_SORTING_FIELD("Incorrect field for sorting was passed", 44601);


    private String message;
    private int code;

    ResponseError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
