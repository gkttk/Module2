package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.uri_builder.UriBuilder;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for GiftCertificateDao.
 */
@Component
public class GiftCertificateModelAssembler extends RepresentationModelAssemblerSupport<GiftCertificateDto, GiftCertificateDto> implements ModelAssembler<GiftCertificateDto> {

    private final UriBuilder uriBuilder;

    public GiftCertificateModelAssembler(UriBuilder uriBuilder) {
        super(GiftCertificateController.class, GiftCertificateDto.class);
        this.uriBuilder = uriBuilder;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toModel(Object)}
     *
     * @param entity GiftCertificateDto.
     * @return GiftCertificateDto with links.
     */
    @Override
    public GiftCertificateDto toModel(GiftCertificateDto entity) {

        Long modelId = entity.getId();
        entity.add(linkTo(methodOn(GiftCertificateController.class).getById(modelId)).withSelfRel());
        entity.add(linkTo(methodOn(GiftCertificateController.class).updateCertificate(null, modelId)).withRel(WebLayerConstants.UPDATE));
        entity.add(linkTo(methodOn(GiftCertificateController.class).patchCertificate(null, modelId)).withRel(WebLayerConstants.PARTIAL_UPDATE));
        entity.add(linkTo(methodOn(GiftCertificateController.class).deleteById(modelId)).withRel(WebLayerConstants.DELETE));
        return entity;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, Map)} (Object)}
     * @param entities DTOs for links.
     * @param offset offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of GiftCertificateDto with links.
     */
    public CollectionModel<GiftCertificateDto> toCollectionModel(Iterable<? extends GiftCertificateDto> entities,
                                                                 Integer offset, Map<String, String[]> reqParams) {
        CollectionModel<GiftCertificateDto> collectionModel = super.toCollectionModel(entities);
        String paramsString = uriBuilder.buildRequestParams(reqParams);
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, 0))
                .slash(paramsString)
                .withRel("firstPage"));
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class)
                .getAllForQuery(null, WebLayerConstants.DEFAULT_LIMIT, offset + WebLayerConstants.DEFAULT_LIMIT))
                .slash(paramsString)
                .withRel("nextPage"));

        return collectionModel;
    }

}
