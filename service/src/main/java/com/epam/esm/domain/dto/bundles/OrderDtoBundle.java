package com.epam.esm.domain.dto.bundles;

import com.epam.esm.domain.dto.OrderDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderDtoBundle {
    private final List<OrderDto> orders;
    private final long count;
}
