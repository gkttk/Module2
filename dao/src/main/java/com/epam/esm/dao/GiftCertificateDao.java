package com.epam.esm.dao;

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
     * This method saves GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity without id.
     * @return GiftCertificate entity with generated id.
     * @since 1.0
     */
    GiftCertificate save(GiftCertificate certificate);


    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity.
     * @since 1.0
     */
    Optional<GiftCertificate> getById(long id);

    /**
     * This method get all GiftCertificate entities.
     *
     * @return List of all GiftCertificate entities.
     * @since 1.0
     */
    List<GiftCertificate> getAll();


    /**
     * This method get GiftCertificate entities by part of their description.
     *
     * @param partOfDescription part of GiftCertificate entity's description for search
     * @return List of GiftCertificate entities which was found by part of description.
     * @since 1.0
     */
    List<GiftCertificate> getAllByPartOfDescription(String partOfDescription);


    /**
     * This method get GiftCertificate entities by part of their name.
     *
     * @param partOfName part of GiftCertificate entity's name for search
     * @return List of GiftCertificate entities which was found by part of name.
     * @since 1.0
     */
    List<GiftCertificate> getAllByPartOfName(String partOfName);

    /**
     * This method get all GiftCertificate entities sorted.
     *
     * @param sortingFieldNames fields of GiftCertificate entity for sorting
     * @param sortingOrder      order of sorting(ASC/DESC)
     * @return List of all sorted GiftCertificate entities.
     * @since 1.0
     */
    List<GiftCertificate> getAllSorted(String[] sortingFieldNames, String sortingOrder);

    /**
     * This method get all GiftCertificate entities which is bound with a specific tag.
     *
     * @param tagName name of Tag entity
     * @return List of all GiftCertificate entities which is bound with tag that has name like tagName param.
     * @since 1.0
     */
    List<GiftCertificate> getAllByTagName(String tagName);


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
