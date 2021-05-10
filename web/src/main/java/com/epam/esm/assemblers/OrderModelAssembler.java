package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.uri_builder.UriBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for OrderDto.
 */
@Component
public class OrderModelAssembler extends RepresentationModelAssemblerSupport<OrderDto, OrderDto>
        implements ModelAssembler<OrderDto> {

    private final UriBuilder uriBuilder;

    public OrderModelAssembler(UriBuilder uriBuilder) {
        super(TagController.class, OrderDto.class);
        this.uriBuilder = uriBuilder;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toModel(Object)}
     *
     * @param entity OrderDto.
     * @return OrderDto with links.
     */
    @Override
    public OrderDto toModel(OrderDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(OrderController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(OrderController.class).deleteById(id)).withRel(WebLayerConstants.DELETE));
        return entity;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, Map)} (Object)}
     *
     * @param entities DTOs for links.
     * @param offset offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of OrderDto with links.
     */
    public CollectionModel<OrderDto> toCollectionModel(Iterable<? extends OrderDto> entities,
                                                       Integer offset, Map<String, String[]> reqParams) {
        CollectionModel<OrderDto> collectionModel = super.toCollectionModel(entities);
        String paramsString = uriBuilder.buildRequestParams(reqParams);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, WebLayerConstants.DEFAULT_LIMIT, 0))
                .slash(paramsString)
                .withRel(WebLayerConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllOrdersForUser(null, null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .slash(paramsString)
                .withRel(WebLayerConstants.NEXT_PAGE));

        return collectionModel;
    }

}
