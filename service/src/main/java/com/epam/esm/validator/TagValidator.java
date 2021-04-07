package com.epam.esm.validator;

import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.exceptions.TagException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link EntityValidator} for TagEntity.
 *
 * @since 1.0
 */
@Component
public class TagValidator implements EntityValidator<Tag> {

    private final TagDao tagDao;

    private final static int TAG_NOT_FOUND_ERROR_CODE = 40402;
    private final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;


    public TagValidator(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public Tag validateAndFindByIdIfExist(long tagId) {
        Optional<Tag> foundTagOpt = tagDao.getById(tagId);
        return foundTagOpt.orElseThrow(() ->
                new TagException(TAG_NOT_FOUND_ERROR_CODE, String.format("Can't find a tag with id: %d", tagId)));
    }

    public void validateIfEntityWithGivenNameExist(String tagName) {
        Optional<Tag> foundTagOpt = tagDao.getByName(tagName);
        if (foundTagOpt.isPresent()) {
            throw new TagException(TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, String.format("Tag with name: %s already exist in DB",
                    tagName));
        }
    }


    /**
     * This method throws an exception when Tag entity with given name and another id is present in db.
     *
     * @param tagName name of Tag entity
     * @param tagId   id of Tag entity
     * @throws GiftCertificateException when there is another GiftCertificate entity in db with given name.
     */
    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String tagName, long tagId) {
        Optional<Tag> foundTagOpt = tagDao.getByName(tagName);
        foundTagOpt.ifPresent(tag -> {
            if (!tag.getId().equals(tagId)) {
                throw new TagException(TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE, String.format("Tag with name: %s and id: %d already exits.",
                        tag.getName(), tag.getId()));
            }
        });
    }


}
