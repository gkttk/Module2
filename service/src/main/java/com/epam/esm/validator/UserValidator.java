package com.epam.esm.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link EntityValidator} for User entity.
 *
 * @since 2.0
 */
@Component
public class UserValidator implements EntityValidator<User> {

    private final UserDao userDao;

    @Autowired
    public UserValidator(UserDao userDao) {
        this.userDao = userDao;
    }


    /**
     * This method attempts to get an User entity from db by it's id.
     *
     * @param userId id of the User entity.
     * @return User entity.
     * @throws UserException when there is no entity with given id in db.
     * @since 2.0
     */
    public User validateAndFindByIdIfExist(long userId) {
        return userDao.findById(userId)
                .orElseThrow(() -> new UserException(ApplicationConstants.USER_NOT_FOUND_ERROR_CODE,
                        String.format("Can't find an user with id: %d", userId)));

    }

    /**
     * This method checks if a User entity with given login exists in db.
     *
     * @param login login of the User entity.
     * @throws UserException if there is User entity with given login in db.
     * @since 2.0
     */
    public void validateIfEntityWithGivenNameExist(String login) {
        Optional<User> userOpt = userDao.findByLogin(login);
        if (userOpt.isPresent()) {
            throw new UserException(ApplicationConstants.USER_SUCH_LOGIN_EXISTS_CODE, String.format("User with login: %s already exists", login));
        }
    }

    /**
     * This method checks if User entity with given login and another id is present in db.
     *
     * @param userName name of User entity
     * @param userId   id of User entity
     * @throws UserException when there is another User entity in db with given name.
     * @since 2.0
     */
    @Override
    public void validateIfAnotherEntityWithGivenNameExist(String userName, long userId) {
        Optional<User> foundUserOpt = userDao.findByLogin(userName);
        foundUserOpt.ifPresent(user -> {
            if (!user.getId().equals(userId)) {
                throw new UserException(ApplicationConstants.USER_SUCH_LOGIN_EXISTS_CODE, String.format("User with login: %s and id: %d already exits.",
                        user.getLogin(), user.getId()));
            }
        });
    }


}
