package com.epam.esm.dao;

import com.epam.esm.mappers.GiftCertificateRowMapper;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.List;

public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static String GET_BY_ID_QUERY = "SELECT * FROM gift_certificate WHERE id = ?"; //todo table name
    private final static String GET_ALL_QUERY = "SELECT * FROM gift_certificate"; //todo table name
    private final static String SAVE_QUERY = "INSERT INTO gift_certificate (name, description, price, duration) " +
            "VALUES (?,?,?,?)"; //todo table name
    private final static String UPDATE_QUERY = "UPDATE gift_certificate SET name = ?, description = ?, " +
            "price = ?, duration = ? WHERE id = ?"; //todo table name
    private final static String DELETE_QUERY = "DELETE FROM gift_certificate WHERE id = ?"; //todo table name

    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper; //todo question, maybe through autowired? +  is it threadsafe?

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template) {
        this.template = template;
        this.rowMapper = new GiftCertificateRowMapper();
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
