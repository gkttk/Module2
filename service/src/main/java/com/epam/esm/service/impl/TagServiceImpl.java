package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.exceptions.UserException;
import com.epam.esm.service.TagService;
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
    private final UserDao userDao;
    private final ModelMapper modelMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao, ModelMapper modelMapper) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.modelMapper = modelMapper;
    }

    /**
     * This method gets a list of TagDto according to request parameters, limit and offset.
     *
     * @param reqParams parameters of a request.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of TagDto.
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
     * This method gets Tag entity from dao layer with given id and converts it to TagDto.
     *
     * @param id id of necessary entity.
     * @return TagDto.
     * @since 1.0
     */
    @Override
    public TagDto findById(long id) {
        Tag foundTag = findByIdIfExist(id);
        return modelMapper.map(foundTag, TagDto.class);
    }

    /**
     * This method saves a TagDto into db.
     *
     * @param tagDto DTO for saving without id.
     * @return TagDto.
     * @since 1.0
     */
    @Override
    @Transactional
    public TagDto save(TagDto tagDto) {

        checkIfEntityWithGivenNameExist(tagDto.getName());
        Tag entity = modelMapper.map(tagDto, Tag.class);
        Tag savedEntity = tagDao.save(entity);
        tagDto.setId(savedEntity.getId());
        return tagDto;

    }

    /**
     * This method deletes Tag entity with given id from db.
     *
     * @param id id of deletable Tag entity.
     * @throws TagException if Order entity with given id doesn't exist in db.
     * @since 1.0
     */
    @Transactional
    @Override
    public void delete(long id) {
        boolean isDeleted = tagDao.deleteById(id);
        if (!isDeleted) {
            throw new TagException(ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, String.format("Tag with id: %d is not found in DB", id));
        }
    }

    /**
     * This method gets TagDto with given name.
     *
     * @param tagName name of Tag entity.
     * @return TagDto.
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


    /**
     * This method gets the most widely used tags of the user with given id.
     *
     * @param userId User entity's id.
     * @return list of TagDto.
     * @throws UserException is there is no user with given id in DB.
     * @since 2.0
     */
    @Override
    public List<TagDto> findMostWidelyUsed(long userId) {
        Optional<User> userOpt = userDao.findById(userId);
        if (!userOpt.isPresent()) {
            throw new UserException(ApplicationConstants.USER_NOT_FOUND_ERROR_CODE,
                    String.format("Can't find an user with id: %d", userId));
        }

        return tagDao.findMaxWidelyUsed(userId)
                .stream()
                .map(tag -> modelMapper.map(tag, TagDto.class))
                .collect(Collectors.toList());
    }

    /**
     * This method attempts to get an Tag entity from db by it's id.
     *
     * @param tagId id of the Tag entity.
     * @return Tag entity.
     * @throws TagException when there is no entity with given id in db.
     * @since 1.0
     */
    private Tag findByIdIfExist(long tagId) {
        Optional<Tag> foundTagOpt = tagDao.findById(tagId);
        return foundTagOpt.orElseThrow(() ->
                new TagException(ApplicationConstants.TAG_NOT_FOUND_ERROR_CODE, String.format("Can't find a tag with id: %d", tagId)));
    }

    /**
     * This method checks if a Tag entity with given name exists in db.
     *
     * @param tagName name of the Tag entity.
     * @throws TagException if there is Tag entity with given name in db.
     * @since 1.0
     */
    private void checkIfEntityWithGivenNameExist(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        if (foundTagOpt.isPresent()) {
            throw new TagException(ApplicationConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, String.format("Tag with name: %s already exist in DB",
                    tagName));
        }
    }
}
