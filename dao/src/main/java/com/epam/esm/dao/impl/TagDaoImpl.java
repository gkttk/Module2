package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    private static final String TABLE_NAME = "tag";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name) " +
            "VALUES (?)";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String FIND_BY_NAME_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";

    private final JdbcTemplate template;
    private final RowMapper<Tag> rowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
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
    public Tag save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String name = tag.getName();

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

       // template.update(SAVE_QUERY, name,keyHolder);

        tag.setId(keyHolder.getKey().longValue());
        return tag;
    }

    @Override
    public void delete(long id) {
        template.update(DELETE_QUERY, id);
    }

    @Override
    public Optional<Tag> findByName(String tagName) {
        Tag tag = template.queryForStream(FIND_BY_NAME_QUERY, rowMapper, tagName).findFirst().orElse(null);
        return Optional.ofNullable(tag);
    }
}
