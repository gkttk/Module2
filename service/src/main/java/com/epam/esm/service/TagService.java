package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * This interface represents an api to interact with the Tag dao layer.
 *
 * Implementations : {@link com.epam.esm.service.impl.TagServiceImpl} classes.
 *
 * @since 1.0
 */
public interface TagService {

    TagDto findById(long id);

    List<TagDto> findAll();

    TagDto save(TagDto tag);

    void delete(long id);

    TagDto findByName(String tagName);
}
