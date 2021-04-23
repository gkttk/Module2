package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserService {

    UserDto findById(long id);

    List<UserDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);


}
