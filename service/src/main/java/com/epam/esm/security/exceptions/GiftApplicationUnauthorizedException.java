package com.epam.esm.security.exceptions;

import com.epam.esm.domain.exceptions.GiftApplicationException;

public class GiftApplicationUnauthorizedException extends GiftApplicationException {
    public GiftApplicationUnauthorizedException(String message, int errorCode, Object... params) {
        super(message, errorCode, params);
    }
}
