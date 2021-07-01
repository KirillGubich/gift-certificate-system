package com.epam.esm.repository.exception;

/**
 * Thrown if there is no newly created entity
 */
public class AbsenceOfNewlyCreatedException extends RuntimeException {

    public AbsenceOfNewlyCreatedException() {
    }

    public AbsenceOfNewlyCreatedException(String message) {
        super(message);
    }
}
