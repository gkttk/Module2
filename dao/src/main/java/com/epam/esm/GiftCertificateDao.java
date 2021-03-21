package com.epam.esm;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    GiftCertificate getById(long id);

    List<GiftCertificate> findAll();

    void save(GiftCertificate certificate);

    void update(GiftCertificate certificate);

    void delete(long id);

}
