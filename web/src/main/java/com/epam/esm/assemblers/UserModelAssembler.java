package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for UserDto.
 */
@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDto>
        implements ModelAssembler<UserDto> {

    public UserModelAssembler() {
        super(TagController.class, UserDto.class);
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
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer)} (Object)}
     *
     * @param entities list of UserDto.
     * @return list of UserDto with links.
     */
    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends UserDto> entities, Integer offset) {
        CollectionModel<UserDto> collectionModel = super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, 0)).withRel(WebLayerConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .withRel(WebLayerConstants.NEXT_PAGE));

        return collectionModel;
    }

}
