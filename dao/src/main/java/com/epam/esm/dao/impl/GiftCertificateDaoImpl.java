package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static String TABLE_NAME = "gift_certificate";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name, description, price, duration) " +
            "VALUES (?,?,?,?)";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, " +
            "price = ?, duration = ? WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private final static String GET_ALL_BY_TAG_NAME = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration," +
            " gc.create_date, gc.last_update_date FROM " + TABLE_NAME + " gc JOIN certificates_tags ct on gc.id = ct.certificate_id" +
            " JOIN tag t on t.id = ct.tag_id WHERE t.name = ?";


    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<GiftCertificate> findAllByTagName(String tagName) {
       return template.query(GET_ALL_BY_TAG_NAME, rowMapper, tagName);
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        GiftCertificate result = template.queryForStream(GET_BY_ID_QUERY, rowMapper, id).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return template.query(GET_ALL_QUERY, rowMapper);
    }

    @Override
    public GiftCertificate save(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        int duration = certificate.getDuration();

        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setBigDecimal(3, price);
            ps.setInt(4, duration);
            return ps;
        }, keyHolder);
        // template.update(SAVE_QUERY, name, description, price, duration, keyHolder);

        certificate.setId(keyHolder.getKey().longValue());
        return certificate;
    }

    @Override
    public void update(GiftCertificate certificate, long id) {
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        int duration = certificate.getDuration();
        template.update(UPDATE_QUERY, name, description, price, duration, id);
    }

    @Override
    public void delete(long id) {
        template.update(DELETE_QUERY, id);

    }
}
