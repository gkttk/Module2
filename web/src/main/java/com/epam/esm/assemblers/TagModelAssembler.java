package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for TagDto.
 */
@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<TagDto, TagDto>
        implements ModelAssembler<TagDto> {

    public TagModelAssembler() {
        super(TagController.class, TagDto.class);
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toModel(Object)}
     *
     * @param entity TagDto.
     * @return TagDto with links.
     */
    @Override
    public TagDto toModel(TagDto entity) {
        Long id = entity.getId();
        entity.add(linkTo(methodOn(TagController.class).getById(id)).withSelfRel());
        entity.add(linkTo(methodOn(TagController.class).deleteById(id)).withRel(WebLayerConstants.DELETE));

        return entity;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer)} (Object)}
     *
     * @param entities list of TagDto.
     * @return list of TagDto with links.
     */
    public CollectionModel<TagDto> toCollectionModel(Iterable<? extends TagDto> entities, Integer offset) {
        CollectionModel<TagDto> collectionModel = super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, 0)).withRel(WebLayerConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .withRel(WebLayerConstants.NEXT_PAGE));

        return collectionModel;
    }

}
