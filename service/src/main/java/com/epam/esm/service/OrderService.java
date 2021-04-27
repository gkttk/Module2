package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SaveOrderDto;

import java.util.List;
import java.util.Map;

/**
 * This interface represents an api to interact with the Order dao layer.
 * <p>
 * Implementations : {@link com.epam.esm.service.impl.OrderServiceImpl} classes.
 *
 * @since 2.0
 */
public interface OrderService {
    /**
     * Find Order by id and map it to OrderDto.
     *
     * @param id Order id.
     * @return OrderDto
     * @since 2.0
     */
    OrderDto findById(long id);

    /**
     * Save Order.
     *
     * @param saveOrderDtoList dto with information about GiftCertificates and their counts for saving Order.
     * @param userId           id of user for which an order is making.
     * @return saved OrderDto
     * @since 2.0
     */
    OrderDto save(List<SaveOrderDto> saveOrderDtoList, Long userId);

    /**
     * Delete Order.
     *
     * @param id Order id.
     * @since 2.0
     */
    void delete(long id);

    /**
     * Get list of OrderDto according to passed request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of OrderDto.
     * @since 2.0
     */
    List<OrderDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset);
}
