package com.epam.esm.domain.exceptions;

public class GiftApplicationException extends RuntimeException {
    protected final int errorCode;
    protected final Object[] params;

    public GiftApplicationException(String message, int errorCode, Object...params) {
        super(message);
        this.errorCode = errorCode;
        this.params = params;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object[] getParams() {
        return params;
    }
}
