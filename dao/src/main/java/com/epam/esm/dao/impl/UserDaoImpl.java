package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.dao.UserDao} interface.
 *
 * @since 2.0
 */
@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;
    private final QueryBuilder<User> queryBuilder;

    @Autowired
    public UserDaoImpl(EntityManager entityManager, QueryBuilder<User> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of User entities
     * @since 2.0
     */
    @Override
    public List<User> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<User> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * This method get User entity by id.
     *
     * @param id User entity's id.
     * @return Optional of User entity.If there is no User with given id, return Optional.empty().
     * @since 2.0
     */
    @Override
    public Optional<User> findById(long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.detach(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }


    /**
     * This method get list of users with the biggest sum of order costs.
     * There might be several users with the same the biggest sum of order costs.
     *
     * @return list of users with the biggest cost of orders.
     * @since 2.0
     */
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


    /**
     * This method saves User entity.
     *
     * @param user User without id.
     * @return Saved User entity.
     * @since 2.0
     */
    @Override
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);
        return user;
    }


    /**
     * This method get User entity by login.
     *
     * @param login User entity's login.
     * @return Optional of User entity. If there is no User with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<User> findByLogin(String login) {
        TypedQuery<User> query = entityManager.createQuery(ApplicationConstants.GET_USER_BY_LOGIN, User.class)
                .setParameter(ApplicationConstants.LOGIN_NAME_FIELD, login);
        Optional<User> userOpt = query.getResultStream().findFirst();
        userOpt.ifPresent(entityManager::detach);

        return userOpt;
    }
}
