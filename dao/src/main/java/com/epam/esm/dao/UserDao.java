package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    List<User> findBy(Map<String, String[]> reqParams, int limit, int offset);

    User findById(long id);

}
