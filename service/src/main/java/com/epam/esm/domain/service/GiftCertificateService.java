package com.epam.esm.domain.service;

import com.epam.esm.domain.dto.GiftCertificateDto;
import com.epam.esm.domain.dto.bundles.GiftCertificateDtoBundle;

import java.util.Map;

/**
 * This interface represents an api to interact with the GiftCertificate dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.domain.service.impl.GiftCertificateServiceImpl} classes.
 *
 * @since 1.0
 */
public interface GiftCertificateService {

    /**
     * Find GiftCertificate by id and map it to GiftCertificateDto.
     *
     * @param id GiftCertificate id.
     * @return GiftCertificateDto
     * @since 1.0
     */
    GiftCertificateDto findById(long id);

    /**
     * Save GiftCertificate.
     *
     * @param certificate GiftCertificate for saving.
     * @return saved GiftCertificateDto
     * @since 1.0
     */
    GiftCertificateDto save(GiftCertificateDto certificate);

    /**
     * Update GiftCertificate.
     *
     * @param certificate GiftCertificate for full updating.
     * @return updated GiftCertificateDto
     * @since 1.0
     */
    GiftCertificateDto update(GiftCertificateDto certificate, long id);

    /**
     * Patch GiftCertificate.
     *
     * @param giftCertificatePatchDto GiftCertificate for partial updating.
     * @return patched GiftCertificateDto
     * @since 1.0
     */
    GiftCertificateDto patch(GiftCertificateDto giftCertificatePatchDto, long id);

    /**
     * Delete GiftCertificate.
     *
     * @param id GiftCertificate id.
     * @since 1.0
     */
    void delete(long id);

    /**
     * Get list of GiftCertificateDto according to passed request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return dto which stores information about number of GiftCertificates in DB and GiftCertificateDtos.
     * @since 1.0
     */
    GiftCertificateDtoBundle findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);


}
