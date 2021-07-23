package com.epam.esm.service.exception;

/**
 * Thrown when incorrect certificate description provided
 */
public class IncorrectCertificateDescriptionException extends RuntimeException {

    public IncorrectCertificateDescriptionException(String message) {
        super(message);
    }
}
