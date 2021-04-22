package com.epam.esm.assemblers;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDto, UserDto>
        implements ModelAssembler<UserDto> {

    public UserModelAssembler() {
        super(TagController.class, UserDto.class);
    }

    @Override
    public UserDto toModel(UserDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(UserController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(OrderController.class).createOrder(id,null)).withRel(ApplicationConstants.MAKE_ORDER));
        return entity;
    }

    public CollectionModel<UserDto> toCollectionModel(Iterable<? extends UserDto> entities, Integer offset) {
        CollectionModel<UserDto> collectionModel = super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, 0)).withRel(ApplicationConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(UserController.class)
                .getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, offset + ApplicationConstants.DEFAULT_LIMIT))
                .withRel(ApplicationConstants.NEXT_PAGE));

        return collectionModel;
    }

}
