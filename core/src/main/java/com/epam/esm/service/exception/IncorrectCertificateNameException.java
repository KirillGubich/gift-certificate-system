package com.epam.esm.service.exception;

public class IncorrectCertificateNameException extends RuntimeException {

    public IncorrectCertificateNameException() {
    }

    public IncorrectCertificateNameException(String message) {
        super(message);
    }
}
