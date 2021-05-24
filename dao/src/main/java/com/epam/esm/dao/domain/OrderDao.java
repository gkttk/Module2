package com.epam.esm.dao.domain;

import com.epam.esm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This interface represents an api to interact with the Order entity in database.
 *
 * @since 4.0
 */
public interface OrderDao extends JpaRepository<Order, Long> {}
