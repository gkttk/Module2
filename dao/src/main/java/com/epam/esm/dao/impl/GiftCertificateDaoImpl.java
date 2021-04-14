package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.factory.CriteriaFactory;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Default implementation of {@link com.epam.esm.dao.GiftCertificateDao} interface.
 *
 * @since 1.0
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper;
    private final CriteriaFactory<GiftCertificate> criteriaFactory;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper, CriteriaFactory<GiftCertificate> criteriaFactory) {
        this.template = template;
        this.rowMapper = rowMapper;
        this.criteriaFactory = criteriaFactory;
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams an instance of {@link CriteriaFactoryResult} which contains {@link Criteria}
     *                  and arrays of params for searching.
     * @return list of GiftCertificate entities
     * @since 1.0
     */
    public List<GiftCertificate> findBy(Map<String, String[]> reqParams) {
        List<CriteriaFactoryResult<GiftCertificate>> criteriaWithParams = criteriaFactory.getCriteriaWithParams(reqParams);
        return criteriaWithParams.stream()
                .flatMap(criteriaResult -> {
                    Criteria<GiftCertificate> criteria = criteriaResult.getCriteria();
                    String[] params = criteriaResult.getParams();
                    return criteria.find(params).stream();
                })
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> findByName(String name) {
        GiftCertificate result = template.queryForStream(ApplicationConstants.GET_BY_NAME_GC_QUERY, rowMapper, name).findFirst().orElse(null);
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
            PreparedStatement ps = connection.prepareStatement(ApplicationConstants.SAVE_GC_QUERY, Statement.RETURN_GENERATED_KEYS);
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
    public Optional<GiftCertificate> findById(long id) {
        GiftCertificate result = template.queryForStream(ApplicationConstants.GET_BY_ID_GC_QUERY, rowMapper, id).findFirst().orElse(null);
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
        template.update(ApplicationConstants.UPDATE_GC_QUERY, certificate.getName(), certificate.getDescription(),
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
        int updatedRows = template.update(ApplicationConstants.DELETE_GC_QUERY, id);
        return updatedRows >= 1;

    }


}
