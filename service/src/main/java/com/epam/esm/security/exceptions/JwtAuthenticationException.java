package com.epam.esm.security.exceptions;

import com.epam.esm.domain.exceptions.GiftApplicationException;

public class JwtAuthenticationException extends GiftApplicationException {

    public JwtAuthenticationException(String message, int errorCode, Object... params) {
        super(message, errorCode, params);
    }
}
