package com.epam.esm.domain.exceptions;

public class GiftCertificateException extends GiftApplicationException {


    public GiftCertificateException(String message, int errorCode, Object...params) {
        super(message, errorCode, params);
    }
}
