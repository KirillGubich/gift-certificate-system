package com.epam.esm.service.exception;

/**
 * Thrown when illegal duration provided
 */
public class IllegalDurationException extends RuntimeException {

    public IllegalDurationException(String message) {
        super(message);
    }
}
