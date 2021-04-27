package com.epam.esm.assemblers;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.dto.GiftCertificateDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Implementation of {@link com.epam.esm.assemblers.ModelAssembler} for GiftCertificateDao.
 */
@Component
public class GiftCertificateModelAssembler extends RepresentationModelAssemblerSupport<GiftCertificateDto, GiftCertificateDto> implements ModelAssembler<GiftCertificateDto> {

    public GiftCertificateModelAssembler() {
        super(GiftCertificateController.class, GiftCertificateDto.class);
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
        entity.add(linkTo(methodOn(GiftCertificateController.class).updateCertificate(null, modelId)).withRel(ApplicationConstants.UPDATE));
        entity.add(linkTo(methodOn(GiftCertificateController.class).patchCertificate(null, modelId)).withRel(ApplicationConstants.PARTIAL_UPDATE));
        entity.add(linkTo(methodOn(GiftCertificateController.class).deleteById(modelId)).withRel(ApplicationConstants.DELETE));

        return entity;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer)} (Object)}
     *
     * @param entities list of GiftCertificateDto.
     * @return list of GiftCertificateDto with links.
     */
    public CollectionModel<GiftCertificateDto> toCollectionModel(Iterable<? extends GiftCertificateDto> entities, Integer offset) {
        CollectionModel<GiftCertificateDto> collectionModel = super.toCollectionModel(entities);
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class).getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, 0)).withRel("firstPage"));
        collectionModel.add(linkTo(methodOn(GiftCertificateController.class).getAllForQuery(null, ApplicationConstants.DEFAULT_LIMIT, offset + ApplicationConstants.DEFAULT_LIMIT)).withRel("nextPage"));
        return collectionModel;
    }

}
