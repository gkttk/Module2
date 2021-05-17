package com.epam.esm.exceptions;

public class OrderException extends GiftApplicationException {

    public OrderException(String message, int errorCode, Object...params) {
        super(message, errorCode, params);
    }
}
