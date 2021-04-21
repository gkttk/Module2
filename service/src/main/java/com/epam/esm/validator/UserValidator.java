package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.TagException;
import com.epam.esm.exceptions.UserException;
import org.springframework.stereotype.Component;

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

    public User validateAndFindByIdIfExist(long userId) {
      User foundUser = userDao.findById(userId);
      if (foundUser == null){
          throw  new UserException(ApplicationConstants.USER_NOT_FOUND_ERROR_CODE, String.format("Can't find an user with id: %d", userId));
      }
      return foundUser;

    }

    public void validateIfEntityWithGivenNameExist(String userName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String userName, long taguserIdId) {
        throw new UnsupportedOperationException();
    }


}
