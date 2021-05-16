package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.SaveOrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.groups.SaveGroup;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(path = "/users", produces = "application/hal+json")
@Validated
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final ModelAssembler<UserDto> userAssembler;
    private final ModelAssembler<OrderDto> orderAssembler;

    @Autowired
    public UserController(UserService userService, OrderService orderService, ModelAssembler<UserDto> assembler, ModelAssembler<OrderDto> orderAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.userAssembler = assembler;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto foundUser = userService.findById(id);
        return ResponseEntity.ok(userAssembler.toModel(foundUser));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<UserDto>> getAllForQuery(WebRequest request,
                                                                   @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_LIMIT + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                   @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_OFFSET + "") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParams = request.getParameterMap();
        List<UserDto> users = userService.findAllForQuery(reqParams, limit, offset);
        return ResponseEntity.ok(userAssembler.toCollectionModel(users, offset, reqParams));
    }


    @GetMapping(path = "/{userId}/orders")
    public ResponseEntity<CollectionModel<OrderDto>> getAllOrdersForUser(WebRequest webRequest, @PathVariable Long userId,
                                                                         @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_LIMIT + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                         @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_OFFSET + "") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParamMap = new HashMap<>(webRequest.getParameterMap());
        List<OrderDto> orders = orderService.findAllForQuery(userId, reqParamMap, limit, offset);
        CollectionModel<OrderDto> order = CollectionModel.of(orders);
        int resultSize = orders.size();
        if(resultSize == 0){
            return ResponseEntity.ok(order);
        }
        order.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, WebLayerConstants.DEFAULT_LIMIT, 0))
                .withRel(WebLayerConstants.FIRST_PAGE));
        if (resultSize < 5){
            return ResponseEntity.ok(order);
        }
        order.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .withRel(WebLayerConstants.NEXT_PAGE));

        return ResponseEntity.ok(order);
    }

    @PostMapping(path = "/{userId}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable Long userId,
                                                @RequestBody @Valid List<SaveOrderDto> saveOrderDtoList) {
        OrderDto order = orderService.save(saveOrderDtoList, userId);
        return ResponseEntity.ok(orderAssembler.toModel(order));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Validated(SaveGroup.class) @Valid UserDto user) {
        UserDto savedUser = userService.save(user);
        return ResponseEntity.ok(userAssembler.toModel(savedUser));
    }


}
