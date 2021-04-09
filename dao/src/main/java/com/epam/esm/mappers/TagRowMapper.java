package com.epam.esm.mappers;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class maps ResultSet from db to Tag entity.
 */
@Component
public class TagRowMapper implements RowMapper<Tag> {

    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(ApplicationConstants.ID_RS_KEY);
        String name = rs.getString(ApplicationConstants.NAME_TAG_RS_KEY);
        return new Tag(id, name);
    }
}
