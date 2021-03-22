package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.mappers.TagRowMapper;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String TABLE_NAME = "tag";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name) " +
            "VALUES (?)";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private final JdbcTemplate template;
    private final RowMapper<Tag> rowMapper;


    public TagDaoImpl(JdbcTemplate template) {
        this.template = template;
        this.rowMapper = new TagRowMapper();
    }

    @Override
    public Tag getById(long id) {
        return template.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
    }

    @Override
    public List<Tag> findAll() {
        return template.query(GET_ALL_QUERY, rowMapper);
    }

    @Override
    public void save(Tag tag) {
        String name = tag.getName();
        template.update(SAVE_QUERY, name);
    }

    @Override
    public void delete(long id) {
        template.update(DELETE_QUERY, id);
    }
}
