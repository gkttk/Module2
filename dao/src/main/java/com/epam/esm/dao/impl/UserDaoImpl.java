package com.epam.esm.dao.impl;

import com.epam.esm.querybuilder.QueryBuilder;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;
    private final QueryBuilder<User> queryBuilder;

    public UserDaoImpl(EntityManager entityManager, QueryBuilder<User> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }


    @Override
    public List<User> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<User> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList();


    }

    @Override
    public User findById(long id) {
        return entityManager.find(User.class, id);
    }

}
