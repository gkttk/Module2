package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
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
public class UserValidator implements EntityValidator<User> {

    private final UserDao userDao;

    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }

    public User validateAndFindByIdIfExist(long tagId) {
      User foundUser = userDao.findById(tagId);
      if (foundUser == null){
          throw  new TagException(ApplicationConstants.USER_FOUND_ERROR_CODE, String.format("Can't find an user with id: %d", tagId));
      }
      return foundUser;

    }

    public void validateIfEntityWithGivenNameExist(String tagName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String tagName, long tagId) {
        throw new UnsupportedOperationException();
    }


}
