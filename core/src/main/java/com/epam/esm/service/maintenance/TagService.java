package com.epam.esm.service.maintenance;

import com.epam.esm.repository.dao.CommonDao;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements CommonService<TagDto> {

    private final CommonDao<Tag> dao;

    @Autowired
    public TagService(CommonDao<Tag> dao) {
        this.dao = dao;
    }

    @Override
    public boolean create(TagDto entity) {
        return false;
    }

    @Override
    public Optional<TagDto> read(int id) {
        return Optional.empty();
    }

    @Override
    public List<TagDto> readAll() {
        final List<Tag> tags = dao.readAll();
        final ArrayList<TagDto> tagDtos = new ArrayList<>();
        for (Tag tag : tags) {
            final TagDto tagDto = new TagDto(tag.getId(), tag.getName());
            tagDtos.add(tagDto);
        }
        return tagDtos;
    }

    @Override
    public Optional<TagDto> update(TagDto entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
