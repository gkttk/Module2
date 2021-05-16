package com.epam.esm.exceptions;

public class UserException extends GiftApplicationException {

    public UserException(String message, int errorCode, Object...params) {
        super(message, errorCode, params);
    }
}
