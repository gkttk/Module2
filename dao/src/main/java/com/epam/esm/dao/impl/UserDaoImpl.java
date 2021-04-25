package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public List<User> findWithMaxOrderCost() {
        String hqlMaxOrdersCost = "SELECT sum(o.cost) FROM Order o JOIN o.user u GROUP BY u.id ";
        TypedQuery<BigDecimal> query = entityManager.createQuery(hqlMaxOrdersCost, BigDecimal.class);
        query.setMaxResults(1);
        BigDecimal maxCost = query.getSingleResult();

        String hql = "SELECT u FROM User u JOIN u.orders uo GROUP BY u.id HAVING sum(uo.cost) = :maxCost";
        TypedQuery<User> userTypedQuery = entityManager.createQuery(hql, User.class);
        userTypedQuery.setParameter("maxCost", maxCost);

        return userTypedQuery.getResultList();
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);
        return user;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(ApplicationConstants.GET_USER_BY_LOGIN, User.class);
        User user = query.setParameter(ApplicationConstants.LOGIN_NAME_FIELD, login)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return user != null ? Optional.of(user) : Optional.empty();
    }
}
