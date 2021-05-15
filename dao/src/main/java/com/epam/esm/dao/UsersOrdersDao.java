package com.epam.esm.dao;

/**
 * This interface represents an api to interact with the link between User entity
 * and Order entity in database.
 *
 * Implementations : {@link com.epam.esm.dao.impl.UsersOrdersDaoImpl} classes.
 *
 * @since 1.0
 */
public interface UsersOrdersDao {

    /**
     * This method saves a link between User entity and Order entity.
     *
     * @param userId  id of User entity
     * @param orderId id of Order entity
     * @since 2.0
     */
    void save(long userId, long orderId);

}
