package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateTagsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CertificateTagsDaoImpl implements CertificateTagsDao {

    private final JdbcTemplate template;

    private final static String TABLE_NAME = "certificates_tags";
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (?,?)";
    @Autowired
    public CertificateTagsDaoImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void save(long certificateId, long tagId) {
        template.update(SAVE_QUERY, certificateId, tagId);
    }
}
