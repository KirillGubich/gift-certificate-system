package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent gift certificate
 */
public class NoSuchCertificateException extends RuntimeException {

    private final int id;

    public NoSuchCertificateException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
