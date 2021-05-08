package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SaveOrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"rawtypes", "unchecked"})
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private ModelAssembler assemblerMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private OrderService orderServiceMock;

    @Mock
    private WebRequest webRequestMock;

    @InjectMocks
    private UserController userController;
    private static UserDto userDto;
    private static OrderDto orderDto;

    private static final int TEST_LIMIT = 5;
    private static final int TEST_OFFSET = 0;

    @BeforeAll
    static void init() {
        userDto = new UserDto(1L, "log", "pass", "ADMIN");
        orderDto = new OrderDto(1L, BigDecimal.TEN, null, null);
    }

    @Test
    public void testGetById_ReturnHttpStatusOkWithDto() {
        //given
        long userId = userDto.getId();

        when(userServiceMock.findById(userId)).thenReturn(userDto);
        when(assemblerMock.toModel(userDto)).thenReturn(userDto);
        ResponseEntity<UserDto> expected = ResponseEntity.ok(userDto);
        //when
        ResponseEntity<UserDto> result = userController.getById(userId);
        //then
        assertEquals(result, expected);
        verify(userServiceMock).findById(userId);
        verify(assemblerMock).toModel(userDto);
    }

    @Test
    public void testGetAllForQuery_ReturnHttpStatusOkWithListDto() {
        Map<String, String[]> paramMap = new HashMap<>();
        List<UserDto> listDto = Arrays.asList(userDto, userDto);
        when(webRequestMock.getParameterMap()).thenReturn(paramMap);
        when(userServiceMock.findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET)).thenReturn(listDto);
        CollectionModel<UserDto> collectionModel = CollectionModel.of(listDto);
        when(assemblerMock.toCollectionModel(listDto, TEST_OFFSET)).thenReturn(collectionModel);
        ResponseEntity<CollectionModel<UserDto>> expectedResult = ResponseEntity.ok(collectionModel);

        //when
        ResponseEntity<CollectionModel<UserDto>> result = userController.getAllForQuery(webRequestMock, TEST_LIMIT, TEST_OFFSET);
        //then
        assertEquals(result, expectedResult);
        verify(webRequestMock).getParameterMap();
        verify(userServiceMock).findAllForQuery(paramMap, TEST_LIMIT, TEST_OFFSET);
        verify(assemblerMock).toCollectionModel(listDto, TEST_OFFSET);
    }

    @Test
    public void testGetAllOrdersForUser_ReturnHttpStatusOkWithListDto() {
        long userId = userDto.getId();
        List<OrderDto> listDto = Arrays.asList(orderDto, orderDto);

        when(orderServiceMock.findAllForQuery(eq(userId),  anyMap(), eq(TEST_LIMIT), eq(TEST_OFFSET))).thenReturn(listDto);
        //when
        ResponseEntity<CollectionModel<OrderDto>> result = userController.getAllOrdersForUser(webRequestMock, userId, TEST_LIMIT, TEST_OFFSET);
        //then
        result.getBody().getContent().forEach(order -> {
            assertAll(() -> assertEquals(order.getId(), orderDto.getId()),
                    () -> assertEquals(order.getCost(), orderDto.getCost()));
        });
        verify(orderServiceMock).findAllForQuery(eq(userId),anyMap(), eq(TEST_LIMIT), eq(TEST_OFFSET));
    }

    @Test
    public void testCreateOrder_ThereAreNoValidationErrors_ReturnHttpStatusOkWithDto() {
        //given

        long userId = 1;
        SaveOrderDto saveOrderDto = new SaveOrderDto(1L, 2);
        List<SaveOrderDto> saveOrderDtoList = Collections.singletonList(saveOrderDto);
        when(orderServiceMock.save(saveOrderDtoList, userId)).thenReturn(orderDto);
        when(assemblerMock.toModel(orderDto)).thenReturn(orderDto);
        ResponseEntity<OrderDto> expected = ResponseEntity.ok(orderDto);
        //when
        ResponseEntity<OrderDto> result = userController.createOrder(userId, saveOrderDtoList);
        //then
        assertEquals(result, expected);
        verify(orderServiceMock).save(saveOrderDtoList, userId);
        verify(assemblerMock).toModel(orderDto);
    }

    @Test
    public void testSaveUser_ThereAreNoValidationErrors_ReturnHttpStatusOkWithDto() {
        //given
        when(userServiceMock.save(userDto)).thenReturn(userDto);
        when(assemblerMock.toModel(userDto)).thenReturn(userDto);
        ResponseEntity<UserDto> expected = ResponseEntity.ok(userDto);
        //when
        ResponseEntity<UserDto> result = userController.saveUser(userDto);
        //then
        assertEquals(result, expected);
        verify(userServiceMock).save(userDto);
        verify(assemblerMock).toModel(userDto);
    }

}
