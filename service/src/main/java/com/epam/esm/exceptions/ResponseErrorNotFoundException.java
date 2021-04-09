package com.epam.esm.exceptions;

public class ResponseErrorNotFoundException extends RuntimeException {

    public ResponseErrorNotFoundException(String message) {
        super(message);
    }
}
