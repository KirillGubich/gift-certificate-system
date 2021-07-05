package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.TagDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService implements CommonService<TagDto> {
    private final TagDao tagDao;
    private final ConversionService conversionService;

    @Autowired
    public TagService(TagDao tagDao, ConversionService conversionService) {
        this.tagDao = tagDao;
        this.conversionService = conversionService;
    }

    @Override
    @Transactional
    public TagDto create(TagDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Tag tag = tagDao.create(conversionService.convert(dto, Tag.class));
        return conversionService.convert(tag, TagDto.class);
    }

    @Override
    public TagDto read(int id) {
        final Optional<Tag> tagOptional = tagDao.read(id);
        Tag tag = tagOptional
                .orElseThrow(() -> new NoSuchTagException(id));
        return conversionService.convert(tag, TagDto.class);
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = tagDao.readAll();
        return tags.stream()
                .map(tag -> conversionService.convert(tag, TagDto.class))
                .collect(Collectors.toList());
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

    public List<TagDto> readPaginated(int page, int size) {
        int numberOfPages = tagDao.fetchNumberOfPages(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        List<Tag> tags = tagDao.readPaginated(page, size);
        return tags.stream()
                .map(tag -> conversionService.convert(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    public TagDto receiveMostUsedTag() {
        Optional<Tag> tagOptional = tagDao.readMostWidelyUsedTag();
        return tagOptional.map(tag -> conversionService.convert(tag, TagDto.class))
                .orElseThrow(NoSuchTagException::new);
    }

    public int getLastPage(int size) {
        return tagDao.fetchNumberOfPages(size);
    }
}
