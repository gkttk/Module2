package com.epam.esm.dao.impl;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
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
 * Default implementation of {@link com.epam.esm.dao.GiftCertificateDao} interface.
 *
 * @since 1.0
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static String TABLE_NAME = "gift_certificate";
    private final static String GET_BY_ID_QUERY = "SELECT id, name, description, price, duration, create_date," +
            " last_update_date FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name, description, price, duration) " + "VALUES (?,?,?,?)";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, " +
            "price = ?, duration = ? WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private final static String GET_BY_NAME_QUERY = "SELECT id, name, description, price, duration, create_date," +
            " last_update_date FROM " + TABLE_NAME + " WHERE name = ?";

    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    /**
     * This method combines all getList queries.
     *
     * @param criteriaWithParams an instance of {@link CriteriaFactoryResult} which contains {@link com.epam.esm.criteria.Criteria}
     *                           and arrays of params for searching.
     * @return list of GiftCertificate entities
     * @since 1.0
     */
    public List<GiftCertificate> getBy(CriteriaFactoryResult<GiftCertificate> criteriaWithParams) {
        Criteria<GiftCertificate> criteria = criteriaWithParams.getCriteria();
        String[] params = criteriaWithParams.getParams();

        return criteria.find(params);

    }

    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> getByName(String name) {
        GiftCertificate result = template.queryForStream(GET_BY_NAME_QUERY, rowMapper, name).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * This method saves GiftCertificate entity.
     * The method uses KayHolder for getting generated id for GiftCertificate entity from db.
     *
     * @param certificate GiftCertificate entity without id.
     * @return id of inserted GiftCertificate entity
     * @since 1.0
     */
    @Override
    public Long save(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> getById(long id) {
        GiftCertificate result = template.queryForStream(GET_BY_ID_QUERY, rowMapper, id).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }


    /**
     * This method updates all updatable fields for GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity with fields for update.
     * @param id          GiftCertificate entity id.
     * @since 1.0
     */
    @Override
    public void update(GiftCertificate certificate, long id) {
        template.update(UPDATE_QUERY, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), id);
    }

    /**
     * This method delete GiftCertificate entity.
     *
     * @param id GiftCertificate entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        int updatedRows = template.update(DELETE_QUERY, id);
        return updatedRows >= 1;

    }


}
