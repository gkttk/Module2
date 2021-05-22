package com.epam.esm.domain.exceptions;

public class ResponseErrorNotFoundException extends RuntimeException {

    public ResponseErrorNotFoundException(String message) {
        super(message);
    }
}
