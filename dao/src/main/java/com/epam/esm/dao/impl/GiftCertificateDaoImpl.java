package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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

    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    @Override
    public GiftCertificate getById(long id) {
        return template.queryForObject(GET_BY_ID_QUERY, rowMapper, id);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return template.query(GET_ALL_QUERY, rowMapper);
    }

    @Override
    public void save(GiftCertificate certificate) {
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        int duration = certificate.getDuration();
        template.update(SAVE_QUERY, name, description, price, duration);
    }

    @Override
    public void update(GiftCertificate certificate) {
        String name = certificate.getName();
        String description = certificate.getDescription();
        BigDecimal price = certificate.getPrice();
        int duration = certificate.getDuration();
        template.update(UPDATE_QUERY, name, description, price, duration);
    }

    @Override
    public void delete(long id) {
        template.update(DELETE_QUERY, id);

    }
}
