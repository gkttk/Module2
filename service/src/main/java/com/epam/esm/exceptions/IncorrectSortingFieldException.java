package com.epam.esm.exceptions;

public class IncorrectSortingFieldException extends RuntimeException {
    public IncorrectSortingFieldException() {
        super();
    }

    public IncorrectSortingFieldException(String message) {
        super(message);
    }

    public IncorrectSortingFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectSortingFieldException(Throwable cause) {
        super(cause);
    }

    protected IncorrectSortingFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
