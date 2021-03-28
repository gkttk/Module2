package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.EntityNotFoundException;
import com.epam.esm.exceptions.EntityWithSuchNameAlreadyExists;
import com.epam.esm.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED, readOnly = true)
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
        Optional<Tag> tagOpt = tagDao.getById(id);

        Tag tag = tagOpt.orElseThrow(() -> new EntityNotFoundException(String.format("Can't find a tag with id: %d", id)));

        return modelMapper.map(tag, TagDto.class);
    }

    @Override
    public List<TagDto> findAll() {
        List<Tag> entities = tagDao.findAll();
        if (entities.isEmpty()) {
            throw new EntityNotFoundException("There are no tags in DB");
        }
        return entities.stream().map(entity -> modelMapper.map(entity, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public TagDto save(TagDto tagDto) {
        String tagName = tagDto.getName();
        Optional<Tag> tagFromDbOpt = tagDao.findByName(tagName);

        if (tagFromDbOpt.isPresent()) {
            throw new EntityWithSuchNameAlreadyExists(String.format("Tag with name: %s already exist in DB",
                    tagName));
        } else {
            Tag entity = modelMapper.map(tagDto, Tag.class);
            Tag savedEntity = tagDao.save(entity);
            Long tagId = savedEntity.getId();
            tagDto.setId(tagId);
            return tagDto;
        }
    }

    @Override
    public void delete(long id) {
        boolean isDeleted = tagDao.delete(id);
        if (!isDeleted) {
            throw new EntityNotFoundException(String.format("Tag with id: %d is not found in DB",
                    id));
        }
    }

    @Override
    public TagDto findByName(String tagName) {
        Optional<Tag> tagOpt = tagDao.findByName(tagName);
        Tag tag = tagOpt.orElseThrow(() -> new EntityNotFoundException(String.format("Tag with name: %s is not found in DB",
                tagName)));
        return modelMapper.map(tag, TagDto.class);
    }
}
