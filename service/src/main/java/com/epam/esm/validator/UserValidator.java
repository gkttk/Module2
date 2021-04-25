package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.UserException;
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

    public User validateAndFindByIdIfExist(long userId) {
        return userDao.findById(userId)
                .orElseThrow(()->new UserException(ApplicationConstants.USER_NOT_FOUND_ERROR_CODE,
                        String.format("Can't find an user with id: %d", userId)));

    }

    public void validateIfEntityWithGivenNameExist(String login) {
        Optional<User> userOpt = userDao.findByLogin(login);
        if (userOpt.isPresent()) {
            throw new UserException(ApplicationConstants.USER_SUCH_LOGIN_EXISTS_CODE, String.format("User with login: %s already exists", login));
        }
    }

    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String userName, long userId) {
        throw new UnsupportedOperationException();
    }


}
