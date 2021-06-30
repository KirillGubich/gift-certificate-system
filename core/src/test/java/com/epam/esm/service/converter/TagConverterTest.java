package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagConverterTest {

    private TagConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TagConverter();
    }

    @Test
    void convert() {
        Tag tag = new Tag(1, "Name");
        TagDto expected = new TagDto(1, "Name");
        TagDto actual = converter.convert(tag);
        assertEquals(expected, actual);
    }
}