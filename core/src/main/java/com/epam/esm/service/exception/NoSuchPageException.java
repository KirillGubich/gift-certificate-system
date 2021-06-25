package com.epam.esm.service.exception;

public class NoSuchPageException extends RuntimeException {

    private final int page;

    public NoSuchPageException(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }
}
