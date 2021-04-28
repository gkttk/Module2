package com.epam.esm.exceptions;

public class RequestParameterParserException extends RuntimeException {

    private final int errorCode;

    public RequestParameterParserException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
