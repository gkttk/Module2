package com.epam.esm.assemblers;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<OrderDto, OrderDto>
        implements ModelAssembler<OrderDto> {

    public OrderModelAssembler() {
        super(TagController.class, OrderDto.class);
    }

    @Override
    public OrderDto toModel(OrderDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(OrderController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(OrderController.class).deleteById(id)).withRel(ApplicationConstants.DELETE));
        return entity;
    }

    public CollectionModel<OrderDto> toCollectionModel(Iterable<? extends OrderDto> entities, Integer offset) {
        CollectionModel<OrderDto> collectionModel = super.toCollectionModel(entities);

        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, ApplicationConstants.DEFAULT_LIMIT, 0)).withRel(ApplicationConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, ApplicationConstants.DEFAULT_LIMIT, offset + ApplicationConstants.DEFAULT_LIMIT))
                .withRel(ApplicationConstants.NEXT_PAGE));

        return collectionModel;
    }

}
