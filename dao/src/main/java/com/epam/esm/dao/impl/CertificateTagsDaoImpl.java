package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.CertificateTagsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Default implementation of {@link com.epam.esm.dao.CertificateTagsDao} interface.
 *
 * @since 1.0
 */
@Repository
public class CertificateTagsDaoImpl implements CertificateTagsDao {

    private final JdbcTemplate template;

    @Autowired
    public CertificateTagsDaoImpl(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * This method saves link between GiftCertificate entity and Tag entity.
     *
     * @param certificateId id of GiftCertificate entity
     * @param tagId         id of Tag entity
     * @since 1.0
     */
    @Override
    public void save(long certificateId, long tagId) {
        template.update(ApplicationConstants.SAVE_CERTIFICATE_TAGS_QUERY, certificateId, tagId);
    }

    /**
     * This method removes all links between GiftCertificate entity and Tag entity for a specific GiftCertificate.
     *
     * @param certificateId id of GiftCertificate entity
     * @since 1.0
     */
    @Override
    public void deleteAllTagsForCertificate(long certificateId) {
        template.update(ApplicationConstants.DELETE_ALL_TAGS_FOR_CERTIFICATE, certificateId);
    }
}
