package com.epam.esm.advice.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseError {
    ENTITY_NOT_FOUNT("Entity is not found", 40401),
    ENTITY_ALREADY_EXISTS("Entity already exists", 42000),
    INCORRECT_VALIDATION("Incorrect validation", 43501),
    ILLEGAL_REQUEST_PARAMETER("Illegal request parameter", 44600);

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
