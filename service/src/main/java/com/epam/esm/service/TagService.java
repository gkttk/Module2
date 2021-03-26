package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Optional;

public interface TagService {

    TagDto getById(long id);

    List<TagDto> findAll();

    TagDto save(TagDto tag);

    void delete(long id);

    Optional<TagDto> findByName(String tagName);
}
