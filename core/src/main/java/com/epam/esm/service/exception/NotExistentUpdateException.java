package com.epam.esm.service.exception;

/**
 * Thrown when trying to update a non-existent entity
 */
public class NotExistentUpdateException extends RuntimeException {

    private int id;

    public NotExistentUpdateException() {
    }

    public NotExistentUpdateException(String message) {
        super(message);
    }

    public NotExistentUpdateException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
