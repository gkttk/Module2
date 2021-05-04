package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SaveOrderDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exceptions.GiftCertificateException;
import com.epam.esm.exceptions.OrderException;
import com.epam.esm.exceptions.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserDao userDao;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderDto orderDto;
    private Order order;
    private GiftCertificate giftCertificate;
    private GiftCertificateDto giftCertificateDto;
    private User user;
    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;

    @BeforeEach
    void init() {
        user = new User();
        user.setId(1L);
        user.setLogin("Login");
        user.setPassword("Pass");
        user.setRole("ADMIN");

        giftCertificate = new GiftCertificate();
        giftCertificate.setPrice(BigDecimal.TEN);

        giftCertificateDto = new GiftCertificateDto();
        giftCertificateDto.setPrice(BigDecimal.TEN);

        orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setCost(new BigDecimal(30L));
        orderDto.setGiftCertificates(Arrays.asList(giftCertificateDto, giftCertificateDto, giftCertificateDto));
        order = new Order();
        order.setId(1L);
        order.setCost(new BigDecimal(30L));
        order.setUser(user);
        order.setGiftCertificates(Arrays.asList(giftCertificate, giftCertificate, giftCertificate));
    }

    @Test
    public void testFindById_ReturnDto_EntityWithGivenIdIsPresentInDb() {
        //given
        long id = order.getId();
        when(orderDao.findById(id)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);
        //when
        OrderDto result = orderService.findById(id);
        //then
        verify(orderDao).findById(id);
        verify(modelMapper).map(order, OrderDto.class);
        assertEquals(result, orderDto);
    }

    @Test
    public void testFindById_ThrowException_EntityWithGivenIdIsPresentInDb() {
        //given
        long id = order.getId();
        when(orderDao.findById(id)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(OrderException.class, () -> orderService.findById(id));
        verify(orderDao).findById(id);
    }

    @Test
    public void testFindAllForQuery_ListOfDto_ThereAreEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(orderDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Arrays.asList(order, order));
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);

        List<OrderDto> expectedResult = Arrays.asList(orderDto, orderDto);
        //when
        List<OrderDto> result = orderService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(orderDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
        verify(modelMapper, times(2)).map(order, OrderDto.class);
    }

    @Test
    public void testFindAllForQuery_EmptyList_ThereAreNoEntitiesInDb() {
        //given
        Map<String, String[]> reqParams = Collections.emptyMap();
        when(orderDao.findBy(reqParams, TEST_LIMIT, TEST_OFFSET)).thenReturn(Collections.emptyList());

        List<OrderDto> expectedResult = Collections.emptyList();
        //when
        List<OrderDto> result = orderService.findAllForQuery(reqParams, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(orderDao).findBy(reqParams, TEST_LIMIT, TEST_OFFSET);
    }


    @Test
    public void testSave_ReturnDto_WhenUserWithGivenIdExistsInDb() {
        //given
        long userId = 1;
        Order savingOrder = new Order();
        savingOrder.setUser(user);
        savingOrder.setCost(order.getCost());
        savingOrder.setGiftCertificates(order.getGiftCertificates());
        SaveOrderDto saveOrderDto1 = new SaveOrderDto(1L, 1);
        SaveOrderDto saveOrderDto2 = new SaveOrderDto(2L, 2);
        List<SaveOrderDto> saveOrderDtoList = Arrays.asList(saveOrderDto1, saveOrderDto2);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDao.findById(2L)).thenReturn(Optional.of(giftCertificate));
        when(orderDao.save(savingOrder)).thenReturn(order);
        when(modelMapper.map(order, OrderDto.class)).thenReturn(orderDto);
        //when
        OrderDto result = orderService.save(saveOrderDtoList, userId);
        //then
        assertEquals(result, orderDto);
        verify(userDao).findById(userId);
        verify(giftCertificateDao).findById(1L);
        verify(giftCertificateDao).findById(2L);
        verify(orderDao).save(savingOrder);
        verify(modelMapper).map(order, OrderDto.class);
    }

    @Test
    public void testSave_ThrowException_WhenUserWithGivenIdDoesNotExistInDb() {
        //given
        long userId = -1;
        SaveOrderDto saveOrderDto1 = new SaveOrderDto(1L, 1);
        SaveOrderDto saveOrderDto2 = new SaveOrderDto(2L, 2);
        List<SaveOrderDto> saveOrderDtoList = Arrays.asList(saveOrderDto1, saveOrderDto2);
        when(userDao.findById(userId)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(UserException.class, () -> orderService.save(saveOrderDtoList, userId));
        verify(userDao).findById(userId);
    }

    @Test
    public void testSave_ThrowException_WhenGiftCertificateWithPassedIdDoesNotExistInDb() {
        //given
        long userId = 1;
        SaveOrderDto saveOrderDto1 = new SaveOrderDto(1L, 1);
        List<SaveOrderDto> saveOrderDtoList = Collections.singletonList(saveOrderDto1);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(GiftCertificateException.class, () -> orderService.save(saveOrderDtoList, userId));
        verify(userDao).findById(userId);
        verify(giftCertificateDao).findById(1L);
    }

    @Test
    public void testDelete_deleteEntity_EntityWasDeleted() {
        //given
        long orderId = order.getId();
        when(orderDao.deleteById(orderId)).thenReturn(true);
        //when
        orderService.delete(orderId);
        //then
        verify(orderDao).deleteById(orderId);
    }

    @Test
    public void testDelete_ThrowException_EntityWasNotDeleted() {
        //given
        long orderId = order.getId();
        when(orderDao.deleteById(orderId)).thenReturn(false);
        //when
        //then
        assertThrows(OrderException.class, () -> orderService.delete(orderId));
        verify(orderDao).deleteById(orderId);
    }


}
