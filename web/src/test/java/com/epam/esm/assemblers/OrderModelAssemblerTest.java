package com.epam.esm.assemblers;

import com.epam.esm.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class OrderModelAssemblerTest extends AbstractModelAssemblerTest<OrderDto> {

    @Autowired
    private OrderModelAssembler modelAssembler;

    @Override
    protected OrderModelAssembler getModelAssembler() {
        return modelAssembler;
    }

    @Override
    protected OrderDto getDto() {
        return new OrderDto().toBuilder()
                .id(1L)
                .cost(BigDecimal.TEN)
                .build();
    }
}
