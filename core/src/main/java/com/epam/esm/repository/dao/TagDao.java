package com.epam.esm.repository.dao;

import com.epam.esm.repository.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TagDao implements CommonDao<Tag> {

    private static final String GET_TAG_BY_ID_SQL = "SELECT id, name FROM tags WHERE id = ?";
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

    @Override
    public List<Tag> readAll() {
        return jdbcTemplate.query(GET_ALL_TAGS_SQL, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public boolean create(Tag entity) {
        jdbcTemplate.update(CREATE_TAG_SQL); //todo add params, exception handling
        return false;
    }

    @Override
    public Tag update(Tag entity) { //todo remove or return null
        return null;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_TAG_BY_ID_SQL, id) > 0;
    }
}
