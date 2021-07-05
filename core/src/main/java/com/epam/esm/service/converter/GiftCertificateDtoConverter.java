package com.epam.esm.service.converter;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class GiftCertificateDtoConverter implements Converter<GiftCertificateDto, GiftCertificate> {

    @Override
    public GiftCertificate convert(GiftCertificateDto source) {
        return GiftCertificate.builder()
                .withId(source.getId())
                .withName(source.getName())
                .withDescription(source.getDescription())
                .withPrice(source.getPrice())
                .withDuration(source.getDuration())
                .build();
    }
}
