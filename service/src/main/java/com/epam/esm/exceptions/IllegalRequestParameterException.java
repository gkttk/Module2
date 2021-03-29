package com.epam.esm.exceptions;

public class IllegalRequestParameterException extends RuntimeException {
    public IllegalRequestParameterException() {
    }

    public IllegalRequestParameterException(String message) {
        super(message);
    }

    public IllegalRequestParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRequestParameterException(Throwable cause) {
        super(cause);
    }

    public IllegalRequestParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
