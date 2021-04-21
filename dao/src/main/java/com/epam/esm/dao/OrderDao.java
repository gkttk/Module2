package com.epam.esm.dao;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface OrderDao {



    Order save(Order tag);

    Optional<Order> findById(long id);

    boolean delete(long id);

    List<Order> findAll(Long userId);
}
