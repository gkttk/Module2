package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.TagException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link EntityValidator} for Tag entity.
 *
 * @since 1.0
 */
@Component
public class TagValidator implements EntityValidator<Tag> {

    private final TagDao tagDao;

    public TagValidator(TagDao tagDao) {
        this.tagDao = tagDao;
    }


    /**
     * This method attempts to get an Tag entity from db by it's id.
     *
     * @param tagId id of the Tag entity.
     * @return Tag entity.
     * @throws TagException when there is no entity with given id in db.
     * @since 1.0
     */
    public Tag validateAndFindByIdIfExist(long tagId) {
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
    public void validateIfEntityWithGivenNameExist(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        if (foundTagOpt.isPresent()) {
            throw new TagException(ApplicationConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, String.format("Tag with name: %s already exist in DB",
                    tagName));
        }
    }

    /**
     * This method checks if Tag entity with given name and another id is present in db.
     *
     * @param tagName name of Tag entity
     * @param tagId   id of Tag entity
     * @throws TagException when there is another Tag entity in db with given name.
     * @since 1.0
     */
    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String tagName, long tagId) {
        Optional<Tag> foundTagOpt = tagDao.findByName(tagName);
        foundTagOpt.ifPresent(tag -> {
            if (!tag.getId().equals(tagId)) {
                throw new TagException(ApplicationConstants.TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, String.format("Tag with name: %s and id: %d already exits.",
                        tag.getName(), tag.getId()));
            }
        });
    }


}
