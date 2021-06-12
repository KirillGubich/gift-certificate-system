package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent tag
 */
public class NoSuchTagException extends RuntimeException {

    private int id;

    public NoSuchTagException(int id) {
        this.id = id;
    }

    public NoSuchTagException(String message, int id) {
        super(message);
        this.id = id;
    }

    public NoSuchTagException() {
    }

    public NoSuchTagException(String message) {
        super(message);
    }

    public int getId() {
        return id;
    }
}
