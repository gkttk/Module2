package com.epam.esm.dao;


/**
 * This interface represents an api to interact with the link between GiftCertificate entity
 * and Tag entity in database.
 *
 * Implementations : {@link com.epam.esm.dao.impl.CertificateTagsDaoImpl} classes.
 *
 * @since 1.0
 */
public interface CertificateTagsDao {

    /**
     * This method saves link between GiftCertificate entity and Tag entity.
     *
     * @param certificateId id of GiftCertificate entity
     * @param tagId         id of Tag entity
     * @since 1.0
     */
    void save(long certificateId, long tagId);


    /**
     * This method removes all links between GiftCertificate entity and Tag entity for a specific GiftCertificate.
     *
     * @param certificateId id of GiftCertificate entity
     * @since 1.0
     */
    void deleteAllTagsForCertificate(long certificateId);

}
