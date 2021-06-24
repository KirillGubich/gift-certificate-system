package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.CommonDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.converter.TagDtoConverter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService implements CommonService<TagDto> {

    private final CommonDao<Tag> tagDao;
    private final TagConverter tagConverter;
    private final TagDtoConverter tagDtoConverter;

    @Autowired
    public TagService(CommonDao<Tag> tagDao, TagConverter tagConverter, TagDtoConverter tagDtoConverter) {
        this.tagDao = tagDao;
        this.tagConverter = tagConverter;
        this.tagDtoConverter = tagDtoConverter;
    }

    @Override
    @Transactional
    public TagDto create(TagDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Tag tag = tagDao.create(tagDtoConverter.convert(dto));
        return tagConverter.convert(tag);
    }

    @Override
    public TagDto read(int id) {
        final Optional<Tag> tagOptional = tagDao.read(id);
        Tag tag = tagOptional
                .orElseThrow(() -> new NoSuchTagException(id));
        return tagConverter.convert(tag);
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = tagDao.readAll();
        return tags.stream().map(tagConverter::convert).collect(Collectors.toList());
    }

    @Override
    public TagDto update(TagDto dto) {
        throw new UnsupportedOperationException("Tag: update");
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return tagDao.delete(id);
    }
}
