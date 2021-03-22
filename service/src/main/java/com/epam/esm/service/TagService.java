package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto getById(long id);

    List<TagDto> findAll();

    void save(TagDto tag);

    void delete(long id);

}
