package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import com.epam.esm.uri_builder.UriBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for UserDto.
 */
@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDto>
        implements ModelAssembler<UserDto> {

    private final UriBuilder uriBuilder;

    public UserModelAssembler(UriBuilder uriBuilder) {
        super(TagController.class, UserDto.class);
        this.uriBuilder = uriBuilder;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toModel(Object)}
     *
     * @param entity UserDto.
     * @return UserDto with links.
     */
    @Override
    public UserDto toModel(UserDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(UserController.class).createOrder(id, null)).withRel(WebLayerConstants.MAKE_ORDER));
        return entity;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, Map)} (Object)}
     *
     * @param entities DTOs for links.
     * @param offset offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of UserDto with links.
     */
    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends UserDto> entities,
                                                      Integer offset, Map<String, String[]> reqParams) {
        CollectionModel<UserDto> collectionModel = super.toCollectionModel(entities);
        String paramsString = uriBuilder.buildRequestParams(reqParams);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, 0))
                .slash(paramsString)
                .withRel(WebLayerConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .slash(paramsString)
                .withRel(WebLayerConstants.NEXT_PAGE));

        return collectionModel;
    }

}
