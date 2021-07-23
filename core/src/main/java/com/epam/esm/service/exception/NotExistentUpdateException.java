package com.epam.esm.service.exception;

/**
 * Thrown when trying to update a non-existent entity
 */
public class NotExistentUpdateException extends RuntimeException {

    private final int id;

    public NotExistentUpdateException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
