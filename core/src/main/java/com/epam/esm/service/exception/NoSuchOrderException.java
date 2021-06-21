package com.epam.esm.service.exception;

public class NoSuchOrderException extends RuntimeException {

    private final int id;

    public NoSuchOrderException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
