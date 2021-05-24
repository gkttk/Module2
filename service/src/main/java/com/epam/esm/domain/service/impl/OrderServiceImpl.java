package com.epam.esm.domain.service.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.dao.domain.GiftCertificateDao;
import com.epam.esm.dao.domain.OrderDao;
import com.epam.esm.dao.domain.UserDao;
import com.epam.esm.domain.dto.OrderDto;
import com.epam.esm.domain.dto.SaveOrderDto;
import com.epam.esm.domain.dto.bundles.OrderDtoBundle;
import com.epam.esm.domain.exceptions.GiftCertificateException;
import com.epam.esm.domain.exceptions.OrderException;
import com.epam.esm.domain.exceptions.UserException;
import com.epam.esm.domain.service.OrderService;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.domain.service.OrderService} interface.
 *
 * @since 2.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;
    private final CriteriaFindAllDao<Order> findAllDao;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, GiftCertificateDao giftCertificateDao,
                            @Qualifier("orderCriteriaFindAllDao") CriteriaFindAllDao<Order> findAllDao,
                            ModelMapper modelMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftCertificateDao = giftCertificateDao;
        this.findAllDao = findAllDao;
        this.modelMapper = modelMapper;
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
        Order order = foundOrderOpt.orElseThrow(() -> new OrderException(String.format("Can't find an order with id: %d", id),
                ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE, id));

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
        User user = findUserByIdIfExist(userId);

        List<GiftCertificate> certificatesForOrder = new ArrayList<>();
        saveOrderDtoList
                .forEach(saveOrderDto -> {
                    long certificateId = saveOrderDto.getCertificateId();
                    GiftCertificate foundCertificate = checkAndFindGiftCertificateByIdIfExist(certificateId);
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
        if (orderDao.existsById(id)) {
            orderDao.deleteById(id);
        } else {
            throw new OrderException(String.format("Order with id: %d doesn't exist in DB", id),
                    ApplicationConstants.ORDER_NOT_FOUND_ERROR_CODE, id);
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
    public OrderDtoBundle findAllForQuery(long userId, Map<String, String[]> reqParams, int limit, int offset) {
        findUserByIdIfExist(userId);
        reqParams.put(ApplicationConstants.USER_ID_KEY, new String[]{String.valueOf(userId)});
        List<Order> foundOrders = findAllDao.findBy(reqParams, limit, offset);
        List<OrderDto> ordersDto = foundOrders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .collect(Collectors.toList());

        long count = orderDao.count(Example.of(new Order()));//todo

        return new OrderDtoBundle(ordersDto, count);
    }

    /**
     * This method return an User entity if it exists in db.
     *
     * @param id User's id.
     * @return User entity.
     * @throws UserException if there is no entity with given id in db.
     */
    private User findUserByIdIfExist(long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new UserException(String.format("Can't find an user with id: %d", id),
                        ApplicationConstants.USER_NOT_FOUND_ERROR_CODE, id));
    }

    /**
     * This method attempts to get an GiftCertificate entity from db by it's id.
     *
     * @param certificateId id of the GiftCertificate entity.
     * @return GiftCertificate entity.
     * @throws GiftCertificateException when there is no entity with given id in db.
     * @since 1.0
     */
    public GiftCertificate checkAndFindGiftCertificateByIdIfExist(long certificateId) {
        Optional<GiftCertificate> certificateOpt = giftCertificateDao.findById(certificateId);
        return certificateOpt.orElseThrow(() -> new GiftCertificateException(String.format("GiftCertificate with id: %d doesn't exist in DB",
                certificateId), ApplicationConstants.CERTIFICATE_NOT_FOUND_CODE, certificateId));
    }


}
