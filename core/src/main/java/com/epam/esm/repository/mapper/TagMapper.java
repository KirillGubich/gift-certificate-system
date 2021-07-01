package com.epam.esm.repository.mapper;

import com.epam.esm.repository.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        final int id = rs.getInt("tag_id");
        final String name = rs.getString("name");
        return new Tag(id, name);
    }
}
