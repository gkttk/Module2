package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents an api to interact with the Order entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.OrderDaoImpl} classes.
 *
 * @since 2.0
 */
public interface OrderDao {

    /**
     * This method get a number of entity in the db.
     * @return number of Order entity in DB for User with passed userId.
     * @param userId Id of User.
     * @since 2.0
     */
    long count(long userId);

    /**
     * This method saves Order entity.
     *
     * @param order Order entity without id.
     * @return Saved Order entity.
     * @since 2.0
     */
    Order save(Order order);

    /**
     * This method get Order entity by id.
     *
     * @param id Order entity's id.
     * @return Optional of Order entity. If there is no Order with given id, return Optional.empty().
     * @since 2.0
     */
    Optional<Order> findById(long id);

    /**
     * This method delete Order entity.
     *
     * @param id Order entity's id.
     * @return a boolean which shows if Order entity with given id was in db.
     * @since 2.0
     */
    boolean deleteById(long id);

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of Order entities
     * @since 2.0
     */
    List<Order> findBy(Map<String, String[]> reqParams, int limit, int offset);
}
