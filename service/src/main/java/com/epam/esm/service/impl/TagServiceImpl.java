package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.converter.Converter;
import com.epam.esm.service.converter.impl.TagConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final Converter<Tag, TagDto> converter;//todo generics

    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
        this.converter = new TagConverter();//todo explicit instance
    }

    @Override
    public TagDto getById(long id) {
        Tag entity = tagDao.getById(id);
        return converter.convertToDto(entity);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> entities = tagDao.findAll();
        return entities.stream().map(converter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(TagDto tag) {
        Tag entity = converter.convertToEntity(tag);
        tagDao.save(entity);
    }

    @Override
    public void delete(long id) {
        tagDao.delete(id);
    }
}
