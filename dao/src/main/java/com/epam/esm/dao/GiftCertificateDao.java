package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents an api to interact with the GiftCertificate entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.GiftCertificateDaoImpl} classes.
 *
 * @since 1.0
 */
public interface GiftCertificateDao {

    /**
     * This method get a number of entity in the db.
     * @return number of GiftCertificate entity in DB.
     * @since 2.0
     */
    long count();

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of GiftCertificate entities
     * @since 1.0
     */
    List<GiftCertificate> findBy(Map<String, String[]> reqParams, int limit, int offset);

    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 1.0
     */
    Optional<GiftCertificate> findByName(String name);

    /**
     * This method saves GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity without id.
     * @return Saved GiftCertificate entity.
     * @since 1.0
     */
    GiftCertificate save(GiftCertificate certificate);

    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given id, return Optional.empty().
     * @since 1.0
     */
    Optional<GiftCertificate> findById(long id);

    /**
     * This method updates GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity with fields for update.
     * @since 1.0
     */
    GiftCertificate update(GiftCertificate certificate);

    /**
     * This method delete GiftCertificate entity.
     *
     * @param id GiftCertificate entity's id.
     * @return a boolean which shows if GiftCertificate entity with given id was in db.
     * @since 1.0
     */
    boolean delete(long id);

}
