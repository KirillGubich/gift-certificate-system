package com.epam.esm.service.exception;

/**
 * Thrown when trying to access a non-existent page
 */
public class NoSuchPageException extends RuntimeException {

    private final int page;

    public NoSuchPageException(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
