package com.epam.esm.exceptions;

public class IncorrectGiftCertificateSortingFieldException extends RuntimeException {
    public IncorrectGiftCertificateSortingFieldException() {
        super();
    }

    public IncorrectGiftCertificateSortingFieldException(String message) {
        super(message);
    }

    public IncorrectGiftCertificateSortingFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectGiftCertificateSortingFieldException(Throwable cause) {
        super(cause);
    }

    protected IncorrectGiftCertificateSortingFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
