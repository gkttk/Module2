package com.epam.esm.mappers;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper<Tag> {

    private final static String ID = "id";
    private final static String NAME = "name";

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(ID);
        String name = rs.getString(NAME);
        return new Tag(id, name);
    }
}
