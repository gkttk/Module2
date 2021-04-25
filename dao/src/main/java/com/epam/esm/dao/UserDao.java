package com.epam.esm.dao;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    User save(User user);

    List<User> findWithMaxOrderCost();


    List<User> findBy(Map<String, String[]> reqParams, int limit, int offset);









}
