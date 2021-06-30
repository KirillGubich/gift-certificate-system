package com.epam.esm.service.converter;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GiftCertificateConverter implements Converter<GiftCertificate, GiftCertificateDto> {

    private final TagConverter tagConverter;

    @Autowired
    public GiftCertificateConverter(TagConverter tagConverter) {
        this.tagConverter = tagConverter;
    }

    @Override
    public GiftCertificateDto convert(GiftCertificate source) {
        return GiftCertificateDto.builder()
                .withId(source.getId())
                .withName(source.getName())
                .withDescription(source.getDescription())
                .withPrice(source.getPrice())
                .withDuration(source.getDuration())
                .withCreateDate(source.getCreateDate().toString())
                .withLastUpdateDate(source.getLastUpdateDate().toString())
                .withTags(source.getTags().stream()
                        .map(tagConverter::convert).collect(Collectors.toSet()))
                .build();
    }
}
