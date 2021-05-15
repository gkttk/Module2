package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an api to interact with the Tag dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.service.impl.TagServiceImpl} classes.
 *
 * @since 1.0
 */
public interface TagService {
    /**
     * Find Tag by id and map it to GiftCertificateDto.
     *
     * @param id Tag id.
     * @return TagDto
     * @since 1.0
     */
    TagDto findById(long id);

    /**
     * Get list of TagDto according to passed request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of TagDto.
     * @since 1.0
     */
    List<TagDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);

    /**
     * Save Tag.
     *
     * @param tag Tag for saving.
     * @return saved TagDto
     * @since 1.0
     */
    TagDto save(TagDto tag);

    /**
     * Delete Tag.
     *
     * @param id Tag id.
     * @since 1.0
     */
    void delete(long id);

    /**
     * Find Tag by name and map it to GiftCertificateDto.
     *
     * @param tagName Tag name.
     * @return TagDto
     * @since 1.0
     */
    TagDto findByName(String tagName);

    /**
     * Find the most used tags of user with given id.
     * @param userId User entity's id.
     * @return list of TagDto.
     * @since 2.0
     */
    List<TagDto> findMostWidelyUsed(long userId);
}
