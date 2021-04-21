package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.groups.SaveOrderGroup;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/orders", produces = "application/hal+json")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable long id) {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

   @GetMapping
    public ResponseEntity<List<OrderDto>> getAll(@PathVariable Long userId) {
        List<OrderDto> orders = orderService.findAll(userId);
        return ResponseEntity.ok(orders);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId,
                                                @RequestBody @Validated(SaveOrderGroup.class) @Valid OrderDto orderDto) {
        OrderDto order = orderService.save(orderDto, userId);
        return ResponseEntity.ok(order);
    }

}
