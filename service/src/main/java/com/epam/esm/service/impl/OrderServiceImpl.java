package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SaveOrderDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ModelMapper modelMapper;
    private final EntityValidator<User> userValidator;
    private final EntityValidator<GiftCertificate> giftCertificateValidator;

    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper, EntityValidator<User> userValidator, EntityValidator<GiftCertificate> giftCertificateValidator) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @Override
    public OrderDto findById(long id) {
        Optional<Order> foundOrderOpt = orderDao.findById(id);
        Order order = foundOrderOpt.orElseThrow(() -> new OrderException(ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE,
                String.format("Can't find an order with id: %d", id)));

        return modelMapper.map(order, OrderDto.class);
    }


    @Override
    @Transactional
    public OrderDto save(List<SaveOrderDto> saveOrderDtoList, Long userId) {
        User user = userValidator.validateAndFindByIdIfExist(userId);

        List<GiftCertificate> certificatesForOrder = new ArrayList<>();
        saveOrderDtoList
                .forEach(saveOrderDto -> {
                    long certificateId = saveOrderDto.getCertificateId();
                    GiftCertificate foundCertificate = giftCertificateValidator.validateAndFindByIdIfExist(certificateId);
                    int count = saveOrderDto.getCount();
                    for (int i = 0; i < count; i++) {
                        certificatesForOrder.add(foundCertificate);
                    }
                });

        BigDecimal orderPrice = certificatesForOrder.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

        Order order = new Order();
        order.setCost(orderPrice);
        order.setGiftCertificates(certificatesForOrder);
        order.setUser(user);
        Order savedOrder = orderDao.save(order);

        return modelMapper.map(savedOrder, OrderDto.class);
    }


    @Override
    @Transactional
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
