package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderDto findById(long id);

    OrderDto save(OrderDto order, Long userId);

    void delete(long id);

    List<OrderDto> findAllForQuery(Map<String, String[]> reqParams,int limit, int offset);
}
