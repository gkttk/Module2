package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.domain.dto.OrderDto;
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
    protected void addFirstPage(CollectionModel<OrderDto> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts) {
        long userId = Long.parseLong(urlParts[0]);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, uriBuilderResult.getLimit(), WebLayerConstants.DEFAULT_OFFSET))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.FIRST_PAGE));
    }

    @Override
    protected void addNextPage(CollectionModel<OrderDto> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts) {
        int limit = uriBuilderResult.getLimit();
        int offset = uriBuilderResult.getOffset();
        long userId = Long.parseLong(urlParts[0]);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, limit, offset + limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.NEXT_PAGE));
    }

    @Override
    protected void addLastPage(CollectionModel<OrderDto> collectionModel, UriBuilderResult uriBuilderResult, long count, String[] urlParts) {
        int limit = uriBuilderResult.getLimit();
        long userId = Long.parseLong(urlParts[0]);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, userId, limit, (int) count - limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.LAST_PAGE));

    }

    @Override
    protected void addModelLinks(OrderDto dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(OrderController.class).getById(id)).withSelfRel());
        dto.add(linkTo(methodOn(OrderController.class).deleteById(id)).withRel(WebLayerConstants.DELETE));
    }


}
