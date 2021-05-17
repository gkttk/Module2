package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.bundles.UserDtoBundle;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.exceptions.UserException;
import com.epam.esm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.service.UserService} interface.
 *
 * @since 2.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
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
        List<User> foundUsers = userDao.findBy(reqParams, limit, offset);
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
        user.setRole(user.getRole().toUpperCase());
        checkIfEntityWithGivenNameExist(user.getLogin());
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
                        ApplicationConstants.USER_NOT_FOUND_ERROR_CODE, id));
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
