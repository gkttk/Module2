package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.AbstractModelAssembler} for OrderDto.
 */
@Component
public class OrderModelAssembler extends AbstractModelAssembler<OrderDto> {

    @Autowired
    public OrderModelAssembler(UriBuilder uriBuilder) {
        super(uriBuilder);
    }

    @Override
    protected void addFirstPage(CollectionModel<OrderDto> collectionModel, UriBuilderResult uriBuilderResult) {
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, uriBuilderResult.getLimit(), WebLayerConstants.DEFAULT_OFFSET))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.FIRST_PAGE));
    }

    @Override
    protected void addNextPage(CollectionModel<OrderDto> collectionModel, UriBuilderResult uriBuilderResult) {
        int limit = uriBuilderResult.getLimit();
        int offset = uriBuilderResult.getOffset();
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, limit, offset + limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.NEXT_PAGE));
    }

    @Override
    protected void addModelLinks(OrderDto dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(OrderController.class).getById(id)).withSelfRel());
        dto.add(linkTo(methodOn(OrderController.class).deleteById(id)).withRel(WebLayerConstants.DELETE));
    }

}
