package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final EntityManager entityManager;

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
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

    @Transactional
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
    public List<Order> findAll(Long userId) {

        List<Order> result = new ArrayList<>();

        User user = entityManager.find(User.class, userId);
        if (user != null){
            return user.getOrders();
        }
        return result;
        /*String hql = "SELECT o from Order o JOIN o.user ou WHERE ou.id = :userId";
        TypedQuery<Order> query = entityManager.createQuery(hql, Order.class);
        query.setParameter("userId", userId);
        return query.getResultList();*/

    }


}
