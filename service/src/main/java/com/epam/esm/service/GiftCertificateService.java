package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateDto getById(long id);

    List<GiftCertificateDto> findAll();

    void save(GiftCertificateDto certificate);

    void update(GiftCertificateDto certificate);

    void delete(long id);

}
