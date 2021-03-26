package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.EntityWithSuchNameAlreadyExists;
import com.epam.esm.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDto getById(long id) {
        Tag entity = tagDao.getById(id);
        return modelMapper.map(entity, TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> entities = tagDao.findAll();
        return entities.stream().map(entity -> modelMapper.map(entity, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    public void save(TagDto tag) {
        String tagName = tag.getName();
        Optional<Tag> tagFromDb = tagDao.findByName(tagName);
        if (tagFromDb.isPresent()){
           throw new EntityWithSuchNameAlreadyExists(String.format("Tag with name: %s already exist in DB",
                   tagName));
        }else {
            Tag entity = modelMapper.map(tag, Tag.class);
            tagDao.save(entity);
        }
    }

    @Override
    public void delete(long id) {
        tagDao.delete(id);
    }

    @Override
    public Optional<TagDto> findByName(String tagName) {
        Optional<Tag> tagOpt = tagDao.findByName(tagName);
        if(tagOpt.isPresent()){
            Tag tag = tagOpt.get();
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            return Optional.of(tagDto);
        }
        return Optional.empty();
    }
}
