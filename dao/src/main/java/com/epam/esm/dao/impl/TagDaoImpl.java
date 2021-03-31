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


/**
 * Default implementation of {@link com.epam.esm.dao.TagDao} interface.
 *
 * @since 1.0
 */
@Repository
public class TagDaoImpl implements TagDao {

    private static final String TABLE_NAME = "tag";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name) " +
            "VALUES (?)";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String FIND_BY_NAME_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";

    private final static String FIND_ALL_BY_CERTIFICATE_ID = "SELECT t.id, t.name FROM " + TABLE_NAME +
            " t JOIN certificates_tags ct on t.id = ct.tag_id WHERE ct.certificate_id = ?";


    private final JdbcTemplate template;
    private final RowMapper<Tag> rowMapper;

    @Autowired
    public TagDaoImpl(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    /**
     * This method saves Tag entity.
     * The method uses KayHolder for getting generated id for Tag entity from db.
     *
     * @param tag Tag entity without id.
     * @return Tag entity with generated id.
     * @since 1.0
     */
    @Override
    public Tag save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String name = tag.getName();

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder);

        tag.setId(keyHolder.getKey().longValue());
        return tag;
    }


    /**
     * This method get Tag entity by id.
     *
     * @param id Tag entity's id.
     * @return Optional of Tag entity.If there is no Tag with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> getById(long id) {
        Tag tag = template.queryForStream(GET_BY_ID_QUERY, rowMapper, id).findFirst().orElse(null);
        return Optional.ofNullable(tag);
    }

    /**
     * This method get Tag entity by name.
     *
     * @param tagName Tag entity's name.
     * @return Optional of Tag entity.If there is no Tag with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> findByName(String tagName) {
        Tag tag = template.queryForStream(FIND_BY_NAME_QUERY, rowMapper, tagName).findFirst().orElse(null);
        return Optional.ofNullable(tag);
    }

    /**
     * This method get all Tag entities.
     *
     * @return List of all Tag entities.
     * @since 1.0
     */
    @Override
    public List<Tag> findAll() {
        return template.query(GET_ALL_QUERY, rowMapper);
    }

    /**
     * This method get all Tag entities which is bound with a specific GiftCertificate entity.
     *
     * @param certificateId id of GiftCertificate entity
     * @return List of all Tag entities which is bound with GiftCertificate that has id like id-param.
     * @since 1.0
     */
    @Override
    public List<Tag> getAllByCertificateId(long certificateId) {
        return template.query(FIND_ALL_BY_CERTIFICATE_ID, rowMapper, certificateId);
    }

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        int updatedRows = template.update(DELETE_QUERY, id);
        return updatedRows >= 1;
    }


}
