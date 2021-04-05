package com.epam.esm.exceptions;

public class IncorrectTagSortingFieldException extends RuntimeException {

    public IncorrectTagSortingFieldException() {
        super();
    }

    public IncorrectTagSortingFieldException(String message) {
        super(message);
    }

    public IncorrectTagSortingFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectTagSortingFieldException(Throwable cause) {
        super(cause);
    }

    protected IncorrectTagSortingFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
