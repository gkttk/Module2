package com.epam.esm.assemblers;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<TagDto, TagDto>
        implements ModelAssembler<TagDto> {

    public TagModelAssembler() {
        super(TagController.class, TagDto.class);
    }

    @Override
    public TagDto toModel(TagDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(TagController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(GiftCertificateController.class).deleteById(id)).withRel(ApplicationConstants.DELETE));

        return entity;
    }

    public CollectionModel<TagDto> toCollectionModel(Iterable<? extends TagDto> entities, Integer offset) {
        CollectionModel<TagDto> collectionModel = super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, 0)).withRel(ApplicationConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, offset + ApplicationConstants.DEFAULT_LIMIT))
                .withRel(ApplicationConstants.NEXT_PAGE));

        return collectionModel;
    }

}
