package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.GiftCertificatePatchDto;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificateDto> getAllByPartOfDescription(String partOfDescription);


    List<GiftCertificateDto> getAllByPartOfName(String partOfName);

    List<GiftCertificateDto> getAllSorted(List<String> sortingFieldNames, String sortingOrder);

    List<GiftCertificateDto> getAllByTagName(String tagName);


    GiftCertificateDto getById(long id);

    List<GiftCertificateDto> getAll();

    GiftCertificateDto save(GiftCertificateDto certificate);

    void update(GiftCertificateDto certificate, long id);

    void delete(long id);

    void patch(GiftCertificatePatchDto giftCertificatePatchDto, long id);
}
