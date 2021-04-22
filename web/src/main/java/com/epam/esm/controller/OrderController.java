package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.groups.SaveOrderGroup;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(produces = "application/hal+json")
public class OrderController {

    private final OrderService orderService;
    private final ModelAssembler<OrderDto> assembler;

    @Autowired
    public OrderController(OrderService orderService, ModelAssembler<OrderDto> assembler) {
        this.orderService = orderService;
        this.assembler = assembler;
    }

    @GetMapping(path = "/orders/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable long id) {
        OrderDto order = orderService.findById(id);
        return ResponseEntity.ok(assembler.toModel(order));
    }

   @GetMapping(path = "/users/{userId}/orders")
    public ResponseEntity<CollectionModel<OrderDto>> getAllForQuery(WebRequest webRequest, @PathVariable Long userId,
                                                                    @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                    @RequestParam(required = false, defaultValue = "0") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
       Map<String, String[]> reqParamMap =new HashMap<>(webRequest.getParameterMap());
       reqParamMap.put(ApplicationConstants.USER_ID_KEY, new String[]{String.valueOf(userId)});
       List<OrderDto> orders = orderService.findAllForQuery(reqParamMap, limit, offset);

       CollectionModel<OrderDto> order = CollectionModel.of(orders);
       order.add(linkTo(methodOn(OrderController.class)
               .getAllForQuery(null, userId, ApplicationConstants.DEFAULT_LIMIT, 0))
               .withRel(ApplicationConstants.FIRST_PAGE));
       order.add(linkTo(methodOn(OrderController.class)
               .getAllForQuery(null, userId, ApplicationConstants.DEFAULT_LIMIT, offset + ApplicationConstants.DEFAULT_LIMIT))
               .withRel(ApplicationConstants.NEXT_PAGE));

        return ResponseEntity.ok(order);
    }

    @DeleteMapping(path = "/orders/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/users/{userId}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId,
                                                @RequestBody @Validated(SaveOrderGroup.class) @Valid OrderDto orderDto) {
        OrderDto order = orderService.save(orderDto, userId);
        return ResponseEntity.ok(assembler.toModel(order));
    }

}
