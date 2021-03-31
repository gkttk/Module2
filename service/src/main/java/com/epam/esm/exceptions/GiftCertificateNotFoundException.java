package com.epam.esm.exceptions;

public class GiftCertificateNotFoundException extends RuntimeException {

    public GiftCertificateNotFoundException() {
    }

    public GiftCertificateNotFoundException(String message) {
        super(message);
    }

    public GiftCertificateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GiftCertificateNotFoundException(Throwable cause) {
        super(cause);
    }

    public GiftCertificateNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
