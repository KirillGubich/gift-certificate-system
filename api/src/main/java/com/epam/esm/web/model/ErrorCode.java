package com.epam.esm.web.model;

/**
 * Class-holder of error messages
 */
public final class ErrorCode {

    public static final int NOT_FOUND_TAG = 40402;
    public static final int NOT_FOUND_CERTIFICATE = 40401;
    public static final int NOT_FOUND_USER = 40403;
    public static final int NOT_FOUND_ORDER = 40404;
    public static final int ILLEGAL_ARGUMENT = 40007;
    public static final int NOT_EXISTENT_UPDATE = 40006;
    public static final int CREATE_TAG_DUPLICATE = 40008;
    public static final int CREATE_CERTIFICATE_DUPLICATE = 40009;
    public static final int INVALID_DATA = 40010;

    private ErrorCode() {
    }
}
