package com.epam.esm.dao;

public interface CertificateTagsDao {

    void save(long certificateId, long tagId);

    void deleteAllTagsForCertificate(long certificateId);

}
