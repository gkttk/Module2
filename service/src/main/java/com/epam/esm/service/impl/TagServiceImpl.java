package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.exceptions.TagWithSuchNameAlreadyExists;
import com.epam.esm.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.service.TagService} interface.
 *
 * @since 1.0
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
    }


    /**
     * This method get TagDto by Tag entity id.
     *
     * @param id Tag entity id.
     * @return TagDto with id.
     * @throws TagNotFoundException if there is no Tag entity with given id in db.
     * @since 1.0
     */
    @Override
    public TagDto findById(long id) {
        Optional<Tag> foundTagOpt = tagDao.getById(id);

        Tag foundTag = foundTagOpt.orElseThrow(() -> new TagNotFoundException(String.format("Can't find a tag with id: %d", id)));

        return modelMapper.map(foundTag, TagDto.class);
    }


    /**
     * This method get list of all TagDto from db.
     *
     * @return list of all TagDto from db.
     * @throws TagNotFoundException if there are no Tag entities in db.
     * @since 1.0
     */
    @Override
    public List<TagDto> findAll() {
        List<Tag> entities = tagDao.getAll();
        if (entities.isEmpty()) {
            throw new TagNotFoundException("There are no tags in DB");
        }
        return entities.stream().map(entity -> modelMapper.map(entity, TagDto.class)).collect(Collectors.toList());
    }

    /**
     * This method saves a TagDto into db.
     *
     * @param tagDto DTO for saving without id.
     * @return saved DTO with id.
     * @throws TagWithSuchNameAlreadyExists if Tag entity with a name like DTO already exists in db.
     * @since 1.0
     */
    @Override
    @Transactional
    public TagDto save(TagDto tagDto) {
        String tagName = tagDto.getName();
        Optional<Tag> foundTagOpt = tagDao.getByName(tagName);

        if (foundTagOpt.isPresent()) {
            throw new TagWithSuchNameAlreadyExists(String.format("Tag with name: %s already exist in DB",
                    tagName));
        } else {
            Tag entity = modelMapper.map(tagDto, Tag.class);
            Tag savedEntity = tagDao.save(entity);
            Long tagId = savedEntity.getId();
            tagDto.setId(tagId);
            return tagDto;
        }
    }

    /**
     * This method deletes a Tag entity from db.
     *
     * @param id Tag entity's id for deleting.
     * @throws TagNotFoundException if there is no Tag entity with given id in db.
     * @since 1.0
     */
    @Override
    public void delete(long id) {
        boolean isDeleted = tagDao.delete(id);
        if (!isDeleted) {
            throw new TagNotFoundException(String.format("Tag with id: %d is not found in DB", id));
        }
    }

    /**
     * This method gets DTO with given name.
     *
     * @param tagName name of Tag entity.
     * @return DTO with id with given name.
     * @throws TagNotFoundException is there is no Tag entity with given name in db.
     * @since 1.0
     */
    @Override
    public TagDto findByName(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.getByName(tagName);
        Tag foundTag = foundTagOpt.orElseThrow(() -> new TagNotFoundException(String.format("Tag with name: %s is not found in DB",
                tagName)));
        return modelMapper.map(foundTag, TagDto.class);
    }
}
