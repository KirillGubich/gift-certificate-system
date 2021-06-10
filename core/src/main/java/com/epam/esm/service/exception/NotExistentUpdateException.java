package com.epam.esm.service.exception;

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
