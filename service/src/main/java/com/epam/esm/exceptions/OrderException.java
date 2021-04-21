package com.epam.esm.exceptions;

public class OrderException extends RuntimeException {

    private final int errorCode;

    public OrderException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }
}
