package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an api to interact with the User dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.service.impl.UserServiceImpl} classes.
 *
 * @since 2.0
 */
public interface UserService {

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
     * @return list of UserDto.
     * @since 2.0
     */
    List<UserDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);


    /**
     * Save GiftCertificate.
     *
     * @param user User for saving.
     * @return saved UserDto
     * @since 2.0
     */
    UserDto save(UserDto user);

}
