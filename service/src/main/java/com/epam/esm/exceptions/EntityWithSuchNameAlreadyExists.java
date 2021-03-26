package com.epam.esm.exceptions;

public class EntityWithSuchNameAlreadyExists extends RuntimeException {


    public EntityWithSuchNameAlreadyExists() {
        super();
    }

    public EntityWithSuchNameAlreadyExists(String message) {
        super(message);
    }

    public EntityWithSuchNameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityWithSuchNameAlreadyExists(Throwable cause) {
        super(cause);
    }

    protected EntityWithSuchNameAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
