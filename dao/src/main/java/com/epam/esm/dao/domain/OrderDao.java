package com.epam.esm.dao.domain;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * This interface represents an api to interact with the Order entity in database.
 *
 * @since 4.0
 */
public interface OrderDao extends JpaRepository<Order, Long> {

    @Query(ApplicationConstants.COUNT_ORDER_BY_USER_ID_QUERY)
    long countByUserId(@Param("userId") long userId);


}
