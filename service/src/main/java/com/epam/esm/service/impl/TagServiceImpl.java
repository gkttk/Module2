package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.service.TagService;
import com.epam.esm.sorting.SortingHelper;
import com.epam.esm.validator.EntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
    private final SortingHelper<Tag> sortingHelper;
    private final EntityValidator<Tag> validator;

    @Autowired
    public TagServiceImpl(TagDao tagDao, ModelMapper modelMapper, SortingHelper<Tag> sortingHelper, EntityValidator<Tag> validator) {
        this.tagDao = tagDao;
        this.modelMapper = modelMapper;
        this.sortingHelper = sortingHelper;
        this.validator = validator;
    }

    /**
     * This method do request to dao layer which depends on argument of the method.
     * The method uses {@link com.epam.esm.criteria.factory.TagCriteriaFactory} for getting a correct {@link Criteria} which is based on passed request parameters.
     *
     * @param reqParams parameters of a request.
     * @param limit
     * @param offset
     * @return List of TagDao.
     * @throws TagException is there are no tags in db.
     * @since 1.0
     */
    @Override
    public List<TagDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<Tag> foundTags = tagDao.findBy(reqParams, limit, offset);
        return foundTags.stream()
                .map(entity -> modelMapper.map(entity, TagDto.class))
                .collect(Collectors.toList());
    }


    /**
     * This method get TagDto by Tag entity id.
     *
     * @param id Tag entity id.
     * @return TagDto with id.
     * @throws TagException if there is no Tag entity with given id in db.
     * @since 1.0
     */
    @Override
    public TagDto findById(long id) {
        Tag foundTag = validator.validateAndFindByIdIfExist(id);
        return modelMapper.map(foundTag, TagDto.class);
    }

    /**
     * This method saves a TagDto into db.
     *
     * @param tagDto DTO for saving without id.
     * @return saved DTO with id.
     * @throws TagException if Tag entity with a name like DTO already exists in db.
     * @since 1.0
     */
    @Override
    @Transactional
    public TagDto save(TagDto tagDto) {
        validator.validateIfEntityWithGivenNameExist(tagDto.getName());

        Tag entity = modelMapper.map(tagDto, Tag.class);
        Tag savedEntity = tagDao.save(entity);
        tagDto.setId(savedEntity.getId());
        return tagDto;

    }

    /**
     * This method deletes a Tag entity from db.
     *
     * @param id Tag entity's id for deleting.
     * @throws TagException if there is no Tag entity with given id in db.
     * @since 1.0
     */
    @Transactional
    @Override
    public void delete(long id) {
        boolean isDeleted = tagDao.delete(id);
        if (!isDeleted) {
            throw new TagException(ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, String.format("Tag with id: %d is not found in DB", id));
        }
    }

    /**
     * This method gets DTO with given name.
     *
     * @param tagName name of Tag entity.
     * @return DTO with id with given name.
     * @throws TagException is there is no Tag entity with given name in db.
     * @since 1.0
     */
    @Override
    public TagDto findByName(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        Tag foundTag = foundTagOpt.orElseThrow(() -> new TagException(ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, String.format("Tag with name: %s is not found in DB",
                tagName)));
        return modelMapper.map(foundTag, TagDto.class);
    }
}
