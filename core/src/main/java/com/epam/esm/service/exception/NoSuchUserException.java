package com.epam.esm.service.exception;

public class NoSuchUserException extends RuntimeException {

    private final int id;

    public NoSuchUserException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
