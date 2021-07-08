package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent user
 */
public class NoSuchUserException extends RuntimeException {

    private int id;

    public NoSuchUserException() {
    }

    public NoSuchUserException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
