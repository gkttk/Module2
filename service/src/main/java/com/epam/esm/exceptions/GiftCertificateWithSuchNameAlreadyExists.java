package com.epam.esm.exceptions;

public class GiftCertificateWithSuchNameAlreadyExists extends RuntimeException {


    public GiftCertificateWithSuchNameAlreadyExists() {
        super();
    }

    public GiftCertificateWithSuchNameAlreadyExists(String message) {
        super(message);
    }

    public GiftCertificateWithSuchNameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public GiftCertificateWithSuchNameAlreadyExists(Throwable cause) {
        super(cause);
    }

    protected GiftCertificateWithSuchNameAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
