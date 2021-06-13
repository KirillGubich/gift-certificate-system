package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.CommonDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService implements CommonService<TagDto> {

    private final CommonDao<Tag> dao;

    @Autowired
    public TagService(CommonDao<Tag> dao) {
        this.dao = dao;
    }


    @Override
    public TagDto create(TagDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Tag tag = dao.create(mapToEntity(dto));
        return mapToDto(tag);
    }

    @Override
    public TagDto read(int id) {
        final Optional<Tag> tag = dao.read(id);
        if (!tag.isPresent()) {
            throw new NoSuchTagException(id);
        }
        return mapToDto(tag.get());
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = dao.readAll();
        return tags.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TagDto update(TagDto dto) {
        throw new UnsupportedOperationException("Update operation is not allowed for tag");
    }

    @Override
    public boolean delete(int id) {
        return dao.delete(id);
    }

    private TagDto mapToDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getName());
    }

    private Tag mapToEntity(TagDto tagDto) {
        return new Tag(tagDto.getId(), tagDto.getName());
    }
}
