package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.EntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private final EntityValidator<User> validator;

    @Autowired
    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper, EntityValidator<User> validator) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.validator = validator;
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
        User foundUser = validator.validateAndFindByIdIfExist(id);
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
    public List<UserDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<User> foundUsers = userDao.findBy(reqParams, limit, offset);
        return foundUsers.stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
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
        user.setRole(user.getRole().toUpperCase());//todo
        validator.validateIfEntityWithGivenNameExist(user.getLogin());
        User userEntity = modelMapper.map(user, User.class);
        User savedUser = userDao.save(userEntity);
        return modelMapper.map(savedUser, UserDto.class);
    }
}
