package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.CommonDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public boolean create(TagDto dto) {
        return dao.create(mapToEntity(dto));
    }

    @Override
    public Optional<TagDto> read(int id) {
        final Optional<Tag> tag = dao.read(id);
        return tag.map(this::mapToDto);
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = dao.readAll();
        return tags.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TagDto update(TagDto dto) {
        return null; //todo remove or not
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
