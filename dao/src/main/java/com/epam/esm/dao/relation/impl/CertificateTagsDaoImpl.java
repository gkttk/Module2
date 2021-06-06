package com.epam.esm.dao.relation.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.relation.CertificateTagsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Default implementation of {@link CertificateTagsDao} interface.
 *
 * @since 1.0
 */
@Repository
public class CertificateTagsDaoImpl implements CertificateTagsDao {

    private final EntityManager entityManager;

    @Autowired
    public CertificateTagsDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This method saves a link between GiftCertificate entity and Tag entity.
     *
     * @param certificateId id of GiftCertificate entity
     * @param tagId         id of Tag entity
     * @since 1.0
     */
    @Override
    public void save(long certificateId, long tagId) {
        Query nativeQuery = entityManager.createNativeQuery(ApplicationConstants.SAVE_CERTIFICATE_TAGS_QUERY);
        nativeQuery.setParameter(1, certificateId);
        nativeQuery.setParameter(2, tagId);
        nativeQuery.executeUpdate();
    }

    /**
     * This method removes all links between GiftCertificate entity and Tag entity for a specific GiftCertificate.
     *
     * @param certificateId id of GiftCertificate entity
     * @since 1.0
     */
    @Override
    public void deleteAllTagLinksForCertificateId(long certificateId) {
        entityManager.createNativeQuery(ApplicationConstants.DELETE_ALL_TAGS_FOR_CERTIFICATE)
                .setParameter(1, certificateId).executeUpdate();
    }

    /**
     * This method removes all links between Tag entity and GiftCertificate entity for a specific Tag.
     *
     * @param tagId id of Tag entity
     * @since 2.0
     */
    @Override
    public void deleteAllCertificateLinksForTagId(long tagId) {
        entityManager.createNativeQuery(ApplicationConstants.DELETE_ALL_CERTIFICATES_FOR_TAG)
                .setParameter(1, tagId).executeUpdate();
    }
}
