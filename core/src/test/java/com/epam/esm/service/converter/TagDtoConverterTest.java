package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagDtoConverterTest {

    private TagDtoConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TagDtoConverter();
    }

    @Test
    void convert() {
        Tag expected = new Tag(0, "Name");
        TagDto tagDto = new TagDto(0, "Name");
        Tag actual = converter.convert(tagDto);
        assertEquals(expected, actual);
    }
}