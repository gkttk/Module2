package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an api to interact with the GiftCertificate dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.service.impl.GiftCertificateServiceImpl} classes.
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
     * @return list of GiftCertificateDto.
     * @since 1.0
     */
    List<GiftCertificateDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);


}
