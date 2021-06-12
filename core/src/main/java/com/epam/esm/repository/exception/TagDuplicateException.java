package com.epam.esm.repository.exception;

/**
 * Thrown when trying to create duplicate of tag
 */
public class TagDuplicateException extends RuntimeException {

    public TagDuplicateException() {
    }

    public TagDuplicateException(String message) {
        super(message);
    }

}
