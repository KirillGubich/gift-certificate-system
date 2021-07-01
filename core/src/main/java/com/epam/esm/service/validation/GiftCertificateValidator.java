package com.epam.esm.service.validation;

import com.epam.esm.service.exception.IllegalDurationException;
import com.epam.esm.service.exception.IllegalPriceException;
import com.epam.esm.service.exception.IncorrectCertificateDescriptionException;
import com.epam.esm.service.exception.IncorrectCertificateNameException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GiftCertificateValidator {

    public String validateName(String name) {
        if (name.length() < 3 || name.length() > 80) {
            throw new IncorrectCertificateNameException("length = " + name.length());
        }
        return name;
    }

    public String validateDescription(String description) {
        if (description.length() < 3 || description.length() > 250) {
            throw new IncorrectCertificateDescriptionException("length = " + description.length());
        }
        return description;
    }

    public BigDecimal validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException("value = " + price.doubleValue());
        }
        return price;
    }

    public int validateDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalDurationException("value = " + duration);
        }
        return duration;
    }
}
