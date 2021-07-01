package com.epam.esm.web.model;

/**
 * Class-holder of error messages
 */
public class ErrorCode {

    public static final int NOT_FOUND_TAG = 40402;
    public static final int NOT_FOUND_CERTIFICATE = 40401;
    public static final int NEWLY_CREATED_ABSENCE = 50004;
    public static final int ILLEGAL_ARGUMENT = 40007;
    public static final int NOT_EXISTENT_UPDATE = 40006;
    public static final int CREATE_TAG_DUPLICATE = 40008;
    public static final int CREATE_CERTIFICATE_DUPLICATE = 40009;
    public static final int INVALID_DATA = 40010;

    private ErrorCode() {
    }
}
