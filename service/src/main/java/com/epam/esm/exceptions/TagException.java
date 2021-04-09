package com.epam.esm.exceptions;

public class TagException extends RuntimeException {

    private final int errorCode;

    public TagException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }
}
