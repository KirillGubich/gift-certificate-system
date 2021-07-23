package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.springframework.core.convert.converter.Converter;

public class TagConverter implements Converter<Tag, TagDto> {

    @Override
    public TagDto convert(Tag source) {
        return new TagDto(source.getId(), source.getName());
    }
}
