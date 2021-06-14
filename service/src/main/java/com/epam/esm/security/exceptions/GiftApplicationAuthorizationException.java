package com.epam.esm.security.exceptions;

import com.epam.esm.domain.exceptions.GiftApplicationException;

public class GiftApplicationAuthorizationException extends GiftApplicationException {
    public GiftApplicationAuthorizationException(String message, int errorCode, Object... params) {
        super(message, errorCode, params);
    }
}
