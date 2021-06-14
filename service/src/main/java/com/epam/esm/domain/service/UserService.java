package com.epam.esm.domain.service;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.dto.bundles.UserDtoBundle;

import java.util.Map;

/**
 * This interface represents an api to interact with the User dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.domain.service.impl.UserServiceImpl} classes.
 *
 * @since 2.0
 */
public interface UserService {

    /**
     * Find User by login and password and map it to UserDto.
     *
     * @param login User login.
     * @param password User password.
     * @return UserDto
     * @since 4.0
     */
    UserDto findByLoginAndPassword(String login, String password);

    /**
     * Find User by login and map it to UserDto.
     *
     * @param login User login.
     * @return UserDto
     * @since 3.0
     */
    UserDto findByLogin(String login);

    /**
     * Find User by id and map it to UserDto.
     *
     * @param id User id.
     * @return UserDto
     * @since 2.0
     */
    UserDto findById(long id);

    /**
     * Get list of UserDto according to passed request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return dto which stores information about number of User in DB and UserDtos.
     * @since 2.0
     */
    UserDtoBundle findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);

    /**
     * Save GiftCertificate.
     *
     * @param user User for saving.
     * @return saved UserDto
     * @since 2.0
     */
    UserDto save(UserDto user);
}
