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
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public List<User> findWithMaxOrderCost() {

      /*  String sql = "SELECT u.id, u.login, u.role, sum(o.cost) FROM user u " +
                "JOIN users_orders uo on u.id = uo.user_id " +
                "JOIN orders o on uo.order_id = o.id " +
                "GROUP BY u.id " +
                "having sum(o.cost) = (SELECT sum(o.cost) FROM user u " +
                "JOIN users_orders uo on u.id = uo.user_id " +
                "JOIN orders o on uo.order_id = o.id " +
                "GROUP BY u.id " +
                "ORDER BY sum(o.cost) desc limit 1)";*/

        String hqlMaxOrdersCost = "SELECT sum(o.cost) FROM User u JOIN u.orders o GROUP BY u.id" +
                " order by sum(o.cost) desc";
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
