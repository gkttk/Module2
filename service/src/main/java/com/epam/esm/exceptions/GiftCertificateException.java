package com.epam.esm.exceptions;

public class GiftCertificateException extends RuntimeException {

    private final int errorCode;

    public GiftCertificateException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }


    public int getErrorCode() {
        return errorCode;
    }
}
