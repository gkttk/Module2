package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.domain.dto.OrderDto;
import com.epam.esm.domain.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/orders", produces = "application/hal+json")
public class OrderController {

    private final OrderService orderService;
    private final ModelAssembler<OrderDto> assembler;

    @Autowired
    public OrderController(OrderService orderService, ModelAssembler<OrderDto> assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable long id) {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(assembler.toModel(order));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
