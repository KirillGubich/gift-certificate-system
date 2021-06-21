package com.epam.esm.repository.exception;

/**
 * Thrown when trying to create duplicate of gift certificate
 */
public class GiftCertificateDuplicateException extends RuntimeException {

    public GiftCertificateDuplicateException(String message) {
        super(message);
    }
}
