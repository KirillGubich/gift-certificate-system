package com.epam.esm.repository.exception;

public class GiftCertificateDuplicateException extends RuntimeException {

    public GiftCertificateDuplicateException() {
    }

    public GiftCertificateDuplicateException(String message) {
        super(message);
    }
}
