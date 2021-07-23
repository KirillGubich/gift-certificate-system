package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent tag
 */
public class NoSuchTagException extends RuntimeException {

    private int id;

    public NoSuchTagException(int id) {
        this.id = id;
    }

    public NoSuchTagException() {
    }

    public int getId() {
        return id;
    }
}
