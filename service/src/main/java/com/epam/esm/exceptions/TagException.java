package com.epam.esm.exceptions;

public class TagException extends GiftApplicationException {

    public TagException(String message, int errorCode, Object...params) {
        super(message, errorCode, params);
    }
}
