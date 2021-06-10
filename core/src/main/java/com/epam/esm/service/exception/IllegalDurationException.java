package com.epam.esm.service.exception;

public class IllegalDurationException extends RuntimeException {

    public IllegalDurationException() {
    }

    public IllegalDurationException(String message) {
        super(message);
    }
}
