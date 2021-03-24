package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface GiftCertificateDao {

    GiftCertificate getById(long id);

    List<GiftCertificate> findAll();

    void save(GiftCertificate certificate);

    void update(GiftCertificate certificate, long id);

    void delete(long id);

}
