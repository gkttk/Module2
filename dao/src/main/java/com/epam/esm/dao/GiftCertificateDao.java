package com.epam.esm.dao;

import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents an api to interact with the GiftCertificate entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.GiftCertificateDaoImpl} classes.
 *
 * @since 1.0
 */
public interface GiftCertificateDao {


    /**
     * This method combines all getList queries.
     *
     * @param criteriaWithParams an instance of {@link CriteriaFactoryResult} which contains {@link com.epam.esm.criteria.Criteria}
     *                           and arrays of params for searching.
     * @return list of GiftCertificate entities
     * @since 1.0
     */
    List<GiftCertificate> getBy(CriteriaFactoryResult<GiftCertificate> criteriaWithParams);


    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity.
     * @since 1.0
     */
    Optional<GiftCertificate> getByName(String name);

    /**
     * This method saves GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity without id.
     * @return id of inserted entity
     * @since 1.0
     */
    Long save(GiftCertificate certificate);


    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity.
     * @since 1.0
     */
    Optional<GiftCertificate> getById(long id);


    /**
     * This method updates all updatable fields for GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity with fields for update.
     * @param id          GiftCertificate entity id.
     * @since 1.0
     */
    void update(GiftCertificate certificate, long id);

    /**
     * This method delete GiftCertificate entity.
     *
     * @param id GiftCertificate entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    boolean delete(long id);

}
