package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao {

    List<User> findBy(Map<String, String[]> reqParams, int limit, int offset);

    User findById(long id);

    List<User> findWithMaxOrderCost();

    User save(User user);

    Optional<User> findByLogin(String login);

}
