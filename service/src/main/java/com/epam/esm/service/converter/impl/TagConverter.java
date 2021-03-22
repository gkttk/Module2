package com.epam.esm.service.converter.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.converter.Converter;

public class TagConverter implements Converter<Tag, TagDto> {
    @Override
    public TagDto convertToDto(Tag entity) {
        Long id = entity.getId();
        String name = entity.getName();
        return new TagDto(id, name);
    }

    @Override
    public Tag convertToEntity(TagDto dto) {
        Long id = dto.getId();
        String name = dto.getName();
        return new Tag(id, name);
    }
}
