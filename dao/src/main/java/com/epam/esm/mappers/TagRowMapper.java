package com.epam.esm.mappers;

import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class maps ResultSet from db to Tag entity.
 */
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
