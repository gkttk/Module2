package com.epam.esm.dao.relation.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.relation.UsersOrdersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Default implementation of {@link UsersOrdersDao} interface.
 *
 * @since 2.0
 */
@Repository
public class UsersOrdersDaoImpl implements UsersOrdersDao {

    private final EntityManager entityManager;

    @Autowired
    public UsersOrdersDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This method saves a link between User entity and Order entity.
     *
     * @param userId  id of User entity
     * @param orderId id of Order entity
     * @since 2.0
     */
    @Override
    public void save(long userId, long orderId) {
        Query nativeQuery = entityManager.createNativeQuery(ApplicationConstants.SAVE_USER_ORDER_QUERY);
        nativeQuery.setParameter(1, userId);
        nativeQuery.setParameter(2, orderId);
        nativeQuery.executeUpdate();
    }
}
