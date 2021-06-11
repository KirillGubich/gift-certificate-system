package com.epam.esm.repository.dao;

import com.epam.esm.repository.exception.AbsenceOfNewlyCreatedException;
import com.epam.esm.repository.exception.TagDuplicateException;
import com.epam.esm.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDao implements CommonDao<Tag> {

    private static final String GET_TAG_BY_ID_SQL = "SELECT id, name FROM tags WHERE id = ?";
    private static final String GET_TAG_BY_NAME_SQL = "SELECT id, name FROM tags WHERE name = ?";
    private static final String GET_ALL_TAGS_SQL = "SELECT id, name FROM tags";
    private static final String DELETE_TAG_BY_ID_SQL = "DELETE FROM tags WHERE id = ?";
    private static final String CREATE_TAG_SQL = "INSERT INTO tags (name) VALUES (?)";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Tag> read(int id) {
        final List<Tag> queryResult = jdbcTemplate.query(GET_TAG_BY_ID_SQL, new BeanPropertyRowMapper<>(Tag.class), id);
        return queryResult.stream().findFirst();
    }

    public Optional<Tag> readByName(String name) {
        final List<Tag> queryResult = jdbcTemplate
                .query(GET_TAG_BY_NAME_SQL, new BeanPropertyRowMapper<>(Tag.class), name);
        return queryResult.stream().findFirst();
    }

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(GET_ALL_TAGS_SQL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Tag create(Tag entity) {
        final String name = entity.getName();
        try {
            jdbcTemplate.update(CREATE_TAG_SQL, name);
        } catch (DuplicateKeyException e) {
            throw new TagDuplicateException(entity.getName());
        }
        return readByName(name).orElseThrow(AbsenceOfNewlyCreatedException::new);
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_TAG_BY_ID_SQL, id) > 0;
    }
}
