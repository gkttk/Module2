package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for UserDto.
 */
@Component
public class UserModelAssembler extends AbstractModelAssembler<UserDto> {

    @Autowired
    public UserModelAssembler(UriBuilder uriBuilder) {
        super(uriBuilder);
    }

    @Override
    protected void addFirstPage(CollectionModel<UserDto> collectionModel, UriBuilderResult uriBuilderResult) {
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, uriBuilderResult.getLimit(), WebLayerConstants.DEFAULT_OFFSET))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.FIRST_PAGE));
    }

    @Override
    protected void addNextPage(CollectionModel<UserDto> collectionModel, UriBuilderResult uriBuilderResult) {
        int limit = uriBuilderResult.getLimit();
        int offset = uriBuilderResult.getOffset();
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, limit, offset + limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.NEXT_PAGE));
    }

    @Override
    protected void addModelLinks(UserDto dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).createOrder(id, null)).withRel(WebLayerConstants.MAKE_ORDER));
    }

}
