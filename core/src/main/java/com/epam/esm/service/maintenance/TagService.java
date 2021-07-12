package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NoSuchPageException;
import com.epam.esm.service.exception.NoSuchTagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService implements CommonService<TagDto> {

    private final TagRepository tagRepository;
    private final ConversionService conversionService;

    @Autowired
    public TagService(TagRepository tagRepository, ConversionService conversionService) {
        this.tagRepository = tagRepository;
        this.conversionService = conversionService;
    }

    @Override
    @Transactional
    public TagDto create(TagDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        Tag tag = conversionService.convert(dto, Tag.class);
        if (tag == null) {
            throw new IllegalArgumentException();
        }
        Tag createdTag = tagRepository.saveAndFlush(tag);
        return conversionService.convert(createdTag, TagDto.class);
    }

    @Override
    public TagDto read(int id) {
        final Optional<Tag> tagOptional = tagRepository.findById(id);
        Tag tag = tagOptional
                .orElseThrow(() -> new NoSuchTagException(id));
        return conversionService.convert(tag, TagDto.class);
    }

    @Override
    public List<TagDto> readAll() {
        List<Tag> tags = new ArrayList<>();
        Iterable<Tag> allTags = tagRepository.findAll();
        allTags.forEach(tags::add);
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
    public void delete(int id) {
        tagRepository.deleteById(id);
    }

    public List<TagDto> readPaginated(int page, int size) {
        int numberOfPages = getLastPage(size);
        if (page > numberOfPages) {
            throw new NoSuchPageException(page);
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Tag> allTags = tagRepository.findAll(pageRequest);
        List<Tag> tags = allTags.get().collect(Collectors.toList());
        return tags.stream()
                .map(tag -> conversionService.convert(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    public TagDto receiveMostUsedTag() {
        final Tag tag = tagRepository.findMostWidelyUsedTag();
        if (tag == null) {
            throw new NoSuchTagException();
        }
        return conversionService.convert(tag, TagDto.class);
    }

    public int getLastPage(int size) {
        int count = (int) tagRepository.count();
        int pages = count / size;
        if (count % size > 0) {
            pages++;
        }
        return pages;
    }
}
