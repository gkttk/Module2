package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.OrderException;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.EntityValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final GiftCertificateDao giftCertificateDao;
    private final ModelMapper modelMapper;
    private final EntityValidator<User> userValidator;

    public OrderServiceImpl(OrderDao orderDao, GiftCertificateDao giftCertificateDao, ModelMapper modelMapper, EntityValidator<User> userValidator) {
        this.orderDao = orderDao;
        this.giftCertificateDao = giftCertificateDao;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
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
        User user = userValidator.validateAndFindByIdIfExist(userId);

        List<GiftCertificate> foundCertificates = orderDto.getGiftCertificates().stream()
                .map(giftCertificate -> giftCertificateDao.findById(giftCertificate.getId()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        BigDecimal orderPrice = foundCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        Order order = new Order();
        order.setCost(orderPrice);
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
