package com.epam.esm.service.exception;

/**
 * Thrown when incorrect certificate name provided
 */
public class IncorrectCertificateNameException extends RuntimeException {

    public IncorrectCertificateNameException() {
    }

    public IncorrectCertificateNameException(String message) {
        super(message);
    }
}
