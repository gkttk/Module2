package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.UsersOrdersDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.OrderException;
import com.epam.esm.exceptions.UserException;
import com.epam.esm.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao giftCertificateDao, ModelMapper modelMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDto findById(long id) {
        Optional<Order> foundOrderOpt = orderDao.findById(id);
        Order order = foundOrderOpt.orElseThrow(() -> new OrderException(ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE,
                String.format("Can't find an order with id: %d", id)));

        return modelMapper.map(order, OrderDto.class);
    }

    @Transactional
    @Override
    public OrderDto save(OrderDto orderDto, Long userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new UserException(ApplicationConstants.USER_NOT_FOUND_ERROR_CODE, String.format("Can't find an user with id: %d", userId));
        }

        Order order = modelMapper.map(orderDto, Order.class);

        List<GiftCertificate> foundCertificates = order.getGiftCertificates().stream()
                .map(giftCertificate -> giftCertificateDao.findById(giftCertificate.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        order.setGiftCertificates(foundCertificates);
        order.setUser(user);
        Order savedOrder = orderDao.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Transactional
    @Override
    public void delete(long id) {
        boolean isDeleted = orderDao.delete(id);
        if (!isDeleted) {
            throw new OrderException(ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE, String.format("Order with id: %d doesn't exist in DB", id));
        }
    }


    @Override
    public List<OrderDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<Order> foundOrders = orderDao.findBy(reqParams, limit, offset);
        return foundOrders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .collect(Collectors.toList());
    }
}
