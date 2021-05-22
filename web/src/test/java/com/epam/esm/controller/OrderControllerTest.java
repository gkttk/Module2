package com.epam.esm.controller;

import com.epam.esm.assemblers.OrderModelAssembler;
import com.epam.esm.domain.dto.OrderDto;
import com.epam.esm.domain.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderModelAssembler assemblerMock;

    @Mock
    private OrderService orderServiceMock;

    @InjectMocks
    private OrderController orderController;

    private static OrderDto orderDto;

    @BeforeAll
    static void init() {
        orderDto = new OrderDto(1L, BigDecimal.TEN, null, null);
    }

    @Test
    public void testGetById_ReturnHttpStatusOkWithDto() {
        //given
        Long orderId = orderDto.getId();
        when(orderServiceMock.findById(orderId)).thenReturn(orderDto);
        when(assemblerMock.toModel(orderDto)).thenReturn(orderDto);
        ResponseEntity<OrderDto> expected = ResponseEntity.ok(orderDto);
        //when
        ResponseEntity<OrderDto> result = orderController.getById(orderId);
        //then
        assertEquals(result, expected);
        verify(orderServiceMock).findById(orderId);
        verify(assemblerMock).toModel(orderDto);
    }


    @Test
    public void testDeleteById_ReturnHttpStatusNoContent() {
        //given
        Long tagId = orderDto.getId();
        ResponseEntity<Void> expected = ResponseEntity.noContent().build();
        //when
        ResponseEntity<Void> result = orderController.deleteById(tagId);
        //then
        assertEquals(result, expected);
        verify(orderServiceMock).delete(tagId);
    }


}
