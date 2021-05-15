package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.dao.OrderDao} interface.
 *
 * @since 2.0
 */
@Repository
public class OrderDaoImpl implements OrderDao {

    private final EntityManager entityManager;
    private final QueryBuilder<Order> queryBuilder;

    @Autowired
    public OrderDaoImpl(EntityManager entityManager, QueryBuilder<Order> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method saves Order entity.
     *
     * @param order Order entity without id.
     * @return Saved Order entity.
     * @since 2.0
     */
    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        entityManager.flush();
        entityManager.detach(order);
        return order;
    }

    /**
     * This method get Order entity by id.
     *
     * @param id Order entity's id.
     * @return Optional of Order entity. If there is no Order with given id, return Optional.empty().
     * @since 2.0
     */
    @Override
    public Optional<Order> findById(long id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.detach(order);
            return Optional.of(order);
        }

        return Optional.empty();
    }

    /**
     * This method delete Order entity.
     *
     * @param id Order entity's id.
     * @return a boolean which shows if Order entity with given id was in db.
     * @since 2.0
     */
    @Override
    public boolean deleteById(long id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.remove(order);
            return true;
        }
        return false;
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of Order entities
     * @since 2.0
     */
    @Override
    public List<Order> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<Order> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }


}
