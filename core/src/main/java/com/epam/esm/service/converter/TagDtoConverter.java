package com.epam.esm.service.converter;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class TagDtoConverter implements Converter<TagDto, Tag> {

    @Override
    public Tag convert(TagDto source) {
        Tag tag = new Tag();
        tag.setName(source.getName());
        return tag;
    }
}
