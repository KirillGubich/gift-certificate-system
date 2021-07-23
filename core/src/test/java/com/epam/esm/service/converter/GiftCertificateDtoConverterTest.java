package com.epam.esm.service.converter;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateDtoConverterTest {

    private GiftCertificateDtoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new GiftCertificateDtoConverter();
    }

    @Test
    void convert() {
        GiftCertificate expected = GiftCertificate.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .build();

        GiftCertificate actual = converter.convert(certificateDto);
        assertEquals(expected, actual);
    }
}