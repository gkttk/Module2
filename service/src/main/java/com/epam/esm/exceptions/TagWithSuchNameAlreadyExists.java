package com.epam.esm.exceptions;

public class TagWithSuchNameAlreadyExists extends RuntimeException {


    public TagWithSuchNameAlreadyExists() {
        super();
    }

    public TagWithSuchNameAlreadyExists(String message) {
        super(message);
    }

    public TagWithSuchNameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public TagWithSuchNameAlreadyExists(Throwable cause) {
        super(cause);
    }

    protected TagWithSuchNameAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
