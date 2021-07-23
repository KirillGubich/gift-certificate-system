package com.epam.esm.service.converter;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftCertificateConverterTest {

    @Mock
    private TagConverter tagConverter;

    private GiftCertificateConverter converter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new GiftCertificateConverter(tagConverter);
    }

    @Test
    void convert() {
        HashSet<TagDto> tagDtos = new HashSet<>();
        HashSet<Tag> tags = new HashSet<>();
        TagDto tagDto = new TagDto(1, "Food");
        tagDtos.add(tagDto);
        Tag tag = new Tag(1, "Food");
        tags.add(tag);
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = GiftCertificate.builder()
                .withId(1)
                .withName("TestName")
                .withDescription("Description")
                .withPrice(new BigDecimal("12.56"))
                .withDuration(10)
                .withCreateDate(now)
                .withLastUpdateDate(now)
                .withTags(tags)
                .build();
        GiftCertificateDto expected = GiftCertificateDto.builder().build();
        expected.setId(1);
        expected.setName("TestName");
        expected.setDescription("Description");
        expected.setPrice(new BigDecimal("12.56"));
        expected.setDuration(10);
        expected.setCreateDate(now.toString());
        expected.setLastUpdateDate(now.toString());
        expected.setTags(tagDtos);

        Mockito.when(tagConverter.convert(tag)).thenReturn(tagDto);

        GiftCertificateDto actual = converter.convert(certificate);
        assertEquals(expected, actual);
    }
}