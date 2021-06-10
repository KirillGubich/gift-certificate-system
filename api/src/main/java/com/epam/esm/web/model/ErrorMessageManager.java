package com.epam.esm.web.model;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class ErrorMessageManager {

    public String receiveMessage(String key, Locale locale) {
        ResourceBundle errorMessages = ResourceBundle.getBundle("errors", locale);
        return errorMessages.getString(key);
    }
}
