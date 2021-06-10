package com.epam.esm.service.exception;

public class IncorrectCertificateDescriptionException extends RuntimeException {

    public IncorrectCertificateDescriptionException() {
    }

    public IncorrectCertificateDescriptionException(String message) {
        super(message);
    }
}
