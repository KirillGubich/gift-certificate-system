package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent gift certificate
 */
public class NoSuchCertificateException extends RuntimeException {

    private int id;

    public NoSuchCertificateException() {
    }

    public NoSuchCertificateException(String message) {
        super(message);
    }

    public NoSuchCertificateException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
