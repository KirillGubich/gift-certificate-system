package com.epam.esm.repository.exception;

public class AbsenceOfNewlyCreatedException extends RuntimeException {

    public AbsenceOfNewlyCreatedException() {
    }

    public AbsenceOfNewlyCreatedException(String message) {
        super(message);
    }
}
