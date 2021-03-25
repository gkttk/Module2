package com.epam.esm.dao;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {

   Optional<GiftCertificate> getById(long id);

    List<GiftCertificate> findAll();

    GiftCertificate save(GiftCertificate certificate);

    void update(GiftCertificate certificate, long id);

    void delete(long id);

}
