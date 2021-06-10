package com.epam.esm.repository.exception;

public class TagDuplicateException extends RuntimeException {

    public TagDuplicateException() {
    }

    public TagDuplicateException(String message) {
        super(message);
    }

}
