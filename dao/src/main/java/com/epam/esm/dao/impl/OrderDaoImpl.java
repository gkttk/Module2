package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final EntityManager entityManager;
    private final QueryBuilder<Order> queryBuilder;


    public OrderDaoImpl(EntityManager entityManager, QueryBuilder<Order> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Order save(Order order) {
        entityManager.persist(order);
        entityManager.flush();
        entityManager.detach(order);
        return order;
    }

    @Override
    public Optional<Order> findById(long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Override
    public boolean delete(long id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.remove(order);
            return true;
        }
        return false;
    }


    @Override
    public List<Order> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<Order> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList();
    }


}
