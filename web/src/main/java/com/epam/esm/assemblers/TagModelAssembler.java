package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.uri_builder.UriBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for TagDto.
 */
@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<TagDto, TagDto>
        implements ModelAssembler<TagDto> {

    private final UriBuilder uriBuilder;

    public TagModelAssembler(UriBuilder uriBuilder) {
        super(TagController.class, TagDto.class);
        this.uriBuilder = uriBuilder;
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
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, Map)} (Object)}
     *
     * @param entities DTOs for links.
     * @param offset offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of TagDto with links.
     */
    public CollectionModel<TagDto> toCollectionModel(Iterable<? extends TagDto> entities,
                                                     Integer offset, Map<String, String[]> reqParams) {
        CollectionModel<TagDto> collectionModel = super.toCollectionModel(entities);
        String paramsString = uriBuilder.buildRequestParams(reqParams);
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, 0))
                .slash(paramsString)
                .withRel(WebLayerConstants.FIRST_PAGE));
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .slash(paramsString)
                .withRel(WebLayerConstants.NEXT_PAGE));

        return collectionModel;
    }

}
