package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.EntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final EntityValidator<User> validator;


    public UserServiceImpl(UserDao userDao, ModelMapper modelMapper, EntityValidator<User> validator) {
        this.userDao = userDao;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public UserDto findById(long id) {
        User foundUser = validator.validateAndFindByIdIfExist(id);
        return modelMapper.map(foundUser, UserDto.class);
    }

    @Override
    public List<UserDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<User> foundUsers = userDao.findBy(reqParams, limit, offset);
        return foundUsers.stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

}
