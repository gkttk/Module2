package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.domain.dto.GiftCertificateDto;
import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.AbstractModelAssembler} for GiftCertificateDao.
 */
@Component
public class GiftCertificateModelAssembler extends AbstractModelAssembler<GiftCertificateDto> {

    @Autowired
    public GiftCertificateModelAssembler(UriBuilder uriBuilder) {
        super(uriBuilder);
    }

    @Override
    protected void addFirstPage(CollectionModel<GiftCertificateDto> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts) {
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllForQuery(null, uriBuilderResult.getLimit(), WebLayerConstants.DEFAULT_OFFSET))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.FIRST_PAGE));
    }

    @Override
    protected void addNextPage(CollectionModel<GiftCertificateDto> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts) {
        int limit = uriBuilderResult.getLimit();
        int offset = uriBuilderResult.getOffset();
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllForQuery(null, limit, offset + limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.NEXT_PAGE));
    }

    @Override
    protected void addLastPage(CollectionModel<GiftCertificateDto> collectionModel, UriBuilderResult uriBuilderResult, long count, String[] urlParts) {
        int limit = uriBuilderResult.getLimit();
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllForQuery(null, limit, (int)count - limit))
                .slash(uriBuilderResult.getParamString())
                .withRel(WebLayerConstants.LAST_PAGE));
    }

    @Override
    protected void addModelLinks(GiftCertificateDto dto) {
        Long modelId = dto.getId();
        dto.add(linkTo(methodOn(GiftCertificateController.class).getById(modelId)).withSelfRel());
        dto.add(linkTo(methodOn(GiftCertificateController.class).updateCertificate(null, modelId)).withRel(WebLayerConstants.UPDATE));
        dto.add(linkTo(methodOn(GiftCertificateController.class).patchCertificate(null, modelId)).withRel(WebLayerConstants.PARTIAL_UPDATE));
        dto.add(linkTo(methodOn(GiftCertificateController.class).deleteById(modelId)).withRel(WebLayerConstants.DELETE));
    }

}
