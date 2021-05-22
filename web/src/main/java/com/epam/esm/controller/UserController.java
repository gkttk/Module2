package com.epam.esm.controller;

import com.epam.esm.assemblers.ModelAssembler;
import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.domain.dto.OrderDto;
import com.epam.esm.domain.dto.SaveOrderDto;
import com.epam.esm.domain.dto.TagDto;
import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.dto.bundles.OrderDtoBundle;
import com.epam.esm.domain.dto.bundles.UserDtoBundle;
import com.epam.esm.domain.dto.groups.SaveGroup;
import com.epam.esm.domain.service.OrderService;
import com.epam.esm.domain.service.TagService;
import com.epam.esm.domain.service.UserService;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users", produces = "application/hal+json")
@Validated
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final TagService tagService;
    private final ModelAssembler<UserDto> userAssembler;
    private final ModelAssembler<OrderDto> orderAssembler;
    private final ModelAssembler<TagDto> tagAssembler;

    @Autowired
    public UserController(UserService userService, OrderService orderService, TagService tagService, ModelAssembler<UserDto> assembler, ModelAssembler<OrderDto> orderAssembler, ModelAssembler<TagDto> tagAssembler) {
        this.userService = userService;
        this.orderService = orderService;
        this.tagService = tagService;
        this.userAssembler = assembler;
        this.orderAssembler = orderAssembler;
        this.tagAssembler = tagAssembler;
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
        UserDtoBundle bundle = userService.findAllForQuery(reqParams, limit, offset);
        List<UserDto> users = bundle.getUsers();
        long count = bundle.getCount();
        return ResponseEntity.ok(userAssembler.toCollectionModel(users, offset, count, reqParams));
    }


    @GetMapping(path = "/{userId}/orders")
    public ResponseEntity<CollectionModel<OrderDto>> getAllOrdersForUser(WebRequest webRequest, @PathVariable long userId,
                                                                         @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_LIMIT + "") @Min(value = 0, message = "Limit parameter must be greater or equal 0") Integer limit,
                                                                         @RequestParam(required = false, defaultValue = WebLayerConstants.DEFAULT_OFFSET + "") @Min(value = 0, message = "Offset parameter must be greater or equal 0") Integer offset) {
        Map<String, String[]> reqParamMap = new HashMap<>(webRequest.getParameterMap());
        OrderDtoBundle bundle = orderService.findAllForQuery(userId, reqParamMap, limit, offset);
        List<OrderDto> orders = bundle.getOrders();
        long count = bundle.getCount();

        CollectionModel<OrderDto> order = CollectionModel.of(orders);
        int resultSize = orders.size();
        if (resultSize == 0) {
            return ResponseEntity.ok(order);
        }
        order.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, limit, WebLayerConstants.DEFAULT_OFFSET))
                .withRel(WebLayerConstants.FIRST_PAGE));

       int delta = (int)count-offset;
       if (limit < delta){
           order.add(linkTo(methodOn(UserController.class)
                   .getAllOrdersForUser(null, userId, limit, offset + limit))
                   .withRel(WebLayerConstants.NEXT_PAGE));
       }

        order.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, limit, (int)count - limit))
                .withRel(WebLayerConstants.LAST_PAGE));
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

    @GetMapping("{userId}/most_widely_used_tag")
    public ResponseEntity<List<TagDto>> getMostWidelyUsedTagOfUser(@PathVariable long userId) {
        List<TagDto> tags = tagService.findMostWidelyUsed(userId);
        List<TagDto> tagsWithLinks = tags.stream()
                .map(tagAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tagsWithLinks);
    }


}
