package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.UsersOrdersDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class UsersOrdersDaoImpl implements UsersOrdersDao {

    private final EntityManager entityManager;

    public UsersOrdersDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(long userId, long orderId) {
        Query nativeQuery = entityManager.createNativeQuery(ApplicationConstants.SAVE_USER_ORDER_QUERY);
        nativeQuery.setParameter(1, userId);
        nativeQuery.setParameter(2, orderId);
        nativeQuery.executeUpdate();
    }
}
