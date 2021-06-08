package com.epam.esm.domain.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.dto.bundles.UserDtoBundle;
import com.epam.esm.domain.exceptions.GiftCertificateException;
import com.epam.esm.domain.exceptions.UserException;
import com.epam.esm.domain.service.UserService;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.domain.service.UserService} interface.
 *
 * @since 2.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CriteriaFindAllDao<User> findAllDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, @Qualifier("userCriteriaFindAllDao") CriteriaFindAllDao<User> findAllDao,
                           ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.findAllDao = findAllDao;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find User by login and password and map it to UserDto.
     *
     * @param login    User login.
     * @param password User password.
     * @return UserDto
     * @since 4.0
     */
    @Override
    public UserDto findByLoginAndPassword(String login, String password) {
        User foundUser = userDao.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new UserException(String.format("Invalid user credentials: login: %s, password: %s.",
                        login, password), ApplicationConstants.INVALID_CREDENTIALS_ERROR_CODE, login, password));

        return modelMapper.map(foundUser, UserDto.class);
    }

    /**
     * Find User by login and map it to UserDto.
     *
     * @param login User login.
     * @return UserDto
     * @since 3.0
     */
    @Override
    public UserDto findByLogin(String login) {
        User foundUser = userDao.findByLogin(login)
                .orElseThrow(() -> new UserException(String.format("User with login : %s was not found", login),
                        ApplicationConstants.USER_NOT_FOUND_BY_LOGIN_ERROR_CODE, login));
        return modelMapper.map(foundUser, UserDto.class);
    }

    /**
     * This method gets User entity from dao layer with given id and converts it to UserDto.
     *
     * @param id id of necessary entity.
     * @return UserDto.
     * @since 2.0
     */
    @Override
    public UserDto findById(long id) {
        User foundUser = findByIdIfExist(id);
        return modelMapper.map(foundUser, UserDto.class);
    }

    /**
     * This method gets a list of UserDto according to request parameters, limit and offset.
     *
     * @param reqParams parameters of a request.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of UserDto.
     * @since 2.0
     */
    @Override
    public UserDtoBundle findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<User> foundUsers = findAllDao.findBy(reqParams, limit, offset);
        List<UserDto> userDtos = foundUsers.stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
        long count = userDao.count();


        return new UserDtoBundle(userDtos, count);
    }

    /**
     * This method saves a UserDto into db.
     *
     * @param user DTO for saving without id.
     * @return UserDto.
     * @since 2.0
     */
    @Override
    @Transactional
    public UserDto save(UserDto user) {
        checkIfEntityWithGivenNameExist(user.getLogin());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        User userEntity = modelMapper.map(user, User.class);
        User savedUser = userDao.save(userEntity);
        return modelMapper.map(savedUser, UserDto.class);
    }

    /**
     * This method return an User if it exists.
     *
     * @param id User's id.
     * @return User entity.
     * @throws GiftCertificateException if there is no entity with given id in db.
     */
    private User findByIdIfExist(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new UserException(String.format("Can't find an user with id: %d", id),
                        ApplicationConstants.USER_NOT_FOUND_BY_ID_ERROR_CODE, id));
    }

    /**
     * This method checks if a User entity with given login exists in db.
     *
     * @param login login of the User entity.
     * @throws UserException if there is User entity with given login in db.
     * @since 2.0
     */
    public void checkIfEntityWithGivenNameExist(String login) {
        Optional<User> userOpt = userDao.findByLogin(login);
        if (userOpt.isPresent()) {
            throw new UserException(String.format("User with login: %s already exists", login),
                    ApplicationConstants.USER_SUCH_LOGIN_EXISTS_CODE, login);
        }
    }
}
