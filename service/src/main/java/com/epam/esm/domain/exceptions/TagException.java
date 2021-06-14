package com.epam.esm.domain.exceptions;

public class TagException extends GiftApplicationException {

    public TagException(String message, int errorCode, Object...params) {
        super(message, errorCode, params);
    }
}
