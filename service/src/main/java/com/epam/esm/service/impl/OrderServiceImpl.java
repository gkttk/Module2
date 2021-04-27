package com.epam.esm.service.impl;

import com.epam.esm.constants.ApplicationConstants;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.service.OrderService} interface.
 *
 * @since 2.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final ModelMapper modelMapper;
    private final EntityValidator<User> userValidator;
    private final EntityValidator<GiftCertificate> giftCertificateValidator;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, ModelMapper modelMapper, EntityValidator<User> userValidator, EntityValidator<GiftCertificate> giftCertificateValidator) {
        this.orderDao = orderDao;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    /**
     * This method gets Order entity from dao layer with given id and converts it to OrderDto.
     *
     * @param id id of necessary entity.
     * @return OrderDto.
     * @throws OrderException if there is no entity with given id in database.
     * @since 2.0
     */
    @Override
    public OrderDto findById(long id) {
        Optional<Order> foundOrderOpt = orderDao.findById(id);
        Order order = foundOrderOpt.orElseThrow(() -> new OrderException(ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE,
                String.format("Can't find an order with id: %d", id)));

        return modelMapper.map(order, OrderDto.class);
    }


    /**
     * This method parses SaveOrderDto to GiftCertificates and their quantities and calculates the sum of the saving order.
     * After that the method saves new Order.
     *
     * @param saveOrderDtoList dto with information about GiftCertificates and their quantities for saving Order.
     * @param userId           id of user for which an order is making.
     * @return saved OrderDto.
     * @since 2.0
     */
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


    /**
     * This method deletes Order entity with given id from db.
     *
     * @param id id of deletable Order entity.
     * @throws OrderException if Order entity with given id doesn't exist in db.
     * @since 2.0
     */
    @Override
    @Transactional
    public void delete(long id) {
        boolean isDeleted = orderDao.deleteById(id);
        if (!isDeleted) {
            throw new OrderException(ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE, String.format("Order with id: %d doesn't exist in DB", id));
        }
    }

    /**
     * This method gets a list of OrderDto according to request parameters, limit and offset.
     *
     * @param reqParams parameters of a request.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return list of OrderDto.
     * @since 2.0
     */
    @Override
    public List<OrderDto> findAllForQuery(Map<String, String[]> reqParams, int limit, int offset) {
        List<Order> foundOrders = orderDao.findBy(reqParams, limit, offset);
        return foundOrders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .collect(Collectors.toList());
    }
}
