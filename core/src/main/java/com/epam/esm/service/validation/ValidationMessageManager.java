package com.epam.esm.service.validation;

/**
 * Class which stores validation messages.
 */
public class ValidationMessageManager {
    /**
     * Indicates that tag name is blank.
     */
    public static final String BLANK_TAG_NAME = "Tag name can't be blank.";
    /**
     * Indicates that tag name has wrong length.
     */
    public static final String TAG_NAME_WRONG_SIZE = "Tag name has to be from 3 to 50 characters.";
    /**
     * Indicates that certificate name is blank.
     */
    public static final String BLANK_CERTIFICATE_NAME = "Certificate name can't be blank.";
    /**
     * Indicates that certificate description is blank.
     */
    public static final String BLANK_CERTIFICATE_DESCRIPTION = "Certificate description can't be blank.";
    /**
     * Indicates that certificate name has wrong length.
     */
    public static final String CERTIFICATE_NAME_WRONG_SIZE = "Certificate name has to be from 3 to 80 characters.";
    /**
     * Indicates that certificate description has wrong length.
     */
    public static final String CERTIFICATE_DESCRIPTION_WRONG_SIZE =
            "Certificate description has to be from 3 to 250 characters.";
    /**
     * Indicates that certificate price is not valid.
     */
    public static final String CERTIFICATE_PRICE_INVALID =
            "Certificate price has to be positive, max digits: 10, fraction: 2.";
    /**
     * Indicates that certificate duration is not valid.
     */
    public static final String CERTIFICATE_DURATION_INVALID =
            "Certificate duration has to be positive max digits: 10, fraction: 0.";
}
