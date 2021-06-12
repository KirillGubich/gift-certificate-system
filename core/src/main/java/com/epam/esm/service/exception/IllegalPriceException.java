package com.epam.esm.service.exception;

/**
 * Thrown when illegal price provided
 */
public class IllegalPriceException extends RuntimeException {

    public IllegalPriceException() {
    }

    public IllegalPriceException(String message) {
        super(message);
    }
}
