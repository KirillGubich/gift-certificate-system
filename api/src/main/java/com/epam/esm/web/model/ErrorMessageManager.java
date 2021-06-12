package com.epam.esm.web.model;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class for getting localized messages from bundle.
 */
@Component
public class ErrorMessageManager {

    /**
     * Gets localized message from bundle
     * @param key message key
     * @param locale message locale
     * @return localized message
     */
    public String receiveMessage(String key, Locale locale) {
        ResourceBundle errorMessages = ResourceBundle.getBundle("errors", locale);
        return errorMessages.getString(key);
    }
}
