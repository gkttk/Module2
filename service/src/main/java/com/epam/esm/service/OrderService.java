package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto findById(long id);

    OrderDto save(OrderDto order, Long userId);

    void delete(long id);

    List<OrderDto> findAll(Long userId);
}
