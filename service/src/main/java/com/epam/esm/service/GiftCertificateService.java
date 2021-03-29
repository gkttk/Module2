package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {

    List<GiftCertificateDto> getAllForQuery(Map<String, String[]> reqParams);

    GiftCertificateDto getById(long id);

    GiftCertificateDto save(GiftCertificateDto certificate);

    GiftCertificateDto update(GiftCertificateDto certificate, long id);

    void delete(long id);

    GiftCertificateDto patch(GiftCertificatePatchDto giftCertificatePatchDto, long id);
}
