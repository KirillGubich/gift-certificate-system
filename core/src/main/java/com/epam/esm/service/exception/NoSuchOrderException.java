package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent order
 */
public class NoSuchOrderException extends RuntimeException {

    private final int id;

    public NoSuchOrderException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
