package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.AbstractModelAssembler} for TagDto.
 */
@Component
public class TagModelAssembler extends AbstractModelAssembler<TagDto> {

    @Autowired
    public TagModelAssembler(UriBuilder uriBuilder) {
        super(uriBuilder);
    }

    public void addModelLinks(TagDto dto) {
        Long id = dto.getId();
        dto.add(linkTo(methodOn(TagController.class).getById(id)).withSelfRel());
        dto.add(linkTo(methodOn(TagController.class).deleteById(id)).withRel(WebLayerConstants.DELETE));
    }

    public void addFirstPage(CollectionModel<TagDto> collectionModel, UriBuilderResult uriBuilderResult) {
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, uriBuilderResult.getLimit(), WebLayerConstants.DEFAULT_OFFSET))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.FIRST_PAGE));
    }

    public void addNextPage(CollectionModel<TagDto> collectionModel, UriBuilderResult uriBuilderResult) {
        int limit = uriBuilderResult.getLimit();
        int offset = uriBuilderResult.getOffset();
        collectionModel.add(linkTo(methodOn(TagController.class)
                .getAllForQuery(null, limit, offset + limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.NEXT_PAGE));
    }


}
