package com.epam.esm.security.exceptions;

import com.epam.esm.domain.exceptions.GiftApplicationException;

public class GiftApplicationAccessDeniedException extends GiftApplicationException {
    public GiftApplicationAccessDeniedException(String message, int errorCode, Object... params) {
        super(message, errorCode, params);
    }
}
