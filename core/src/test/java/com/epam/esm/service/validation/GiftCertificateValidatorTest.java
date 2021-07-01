package com.epam.esm.service.validation;

import com.epam.esm.service.exception.IllegalDurationException;
import com.epam.esm.service.exception.IllegalPriceException;
import com.epam.esm.service.exception.IncorrectCertificateDescriptionException;
import com.epam.esm.service.exception.IncorrectCertificateNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GiftCertificateValidatorTest {

    private GiftCertificateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new GiftCertificateValidator();
    }

    @Test
    void validateName_returnTheSameName_whenCorrect() {
        String name = "anyName";
        String actual = validator.validateName(name);
        assertEquals(name, actual);
    }

    @Test
    void validateName_throwException_whenIncorrect() {
        String name = "a";
        assertThrows(IncorrectCertificateNameException.class,
                () -> validator.validateName(name));
    }

    @Test
    void validateDescription_returnTheSameDescription_whenCorrect() {
        String description = "any description";
        String actual = validator.validateDescription(description);
        assertEquals(description, actual);
    }

    @Test
    void validateDescription_throwException_whenIncorrect() {
        String description = "a";
        assertThrows(IncorrectCertificateDescriptionException.class,
                () -> validator.validateDescription(description));
    }

    @Test
    void validatePrice_returnTheSamePrice_whenCorrect() {
        BigDecimal price = new BigDecimal("10.00");
        BigDecimal actual = validator.validatePrice(price);
        assertEquals(price, actual);
    }

    @Test
    void validatePrice_throwException_whenIncorrect() {
        BigDecimal price = new BigDecimal("-10.00");
        assertThrows(IllegalPriceException.class,
                () -> validator.validatePrice(price));
    }

    @Test
    void validateDuration_returnTheSameDuration_whenCorrect() {
        int duration = 5;
        int actual = validator.validateDuration(duration);
        assertEquals(duration, actual);
    }

    @Test
    void validateDuration_throwException_whenIncorrect() {
        int duration = -5;
        assertThrows(IllegalDurationException.class,
                () -> validator.validateDuration(duration));
    }
}