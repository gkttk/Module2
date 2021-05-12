package com.epam.esm.assemblers;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;

import java.util.Map;

public abstract class AbstractModelAssembler<T> implements ModelAssembler<T> {

    private final UriBuilder uriBuilder;

    @Autowired
    protected AbstractModelAssembler(UriBuilder uriBuilder) {
        this.uriBuilder = uriBuilder;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toModel(Object)}
     *
     * @param dto DTO.
     * @return DTO with links.
     */
    public T toModel(T dto) {
        addModelLinks(dto);
        return dto;
    }

    /**
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, Map)} (Object)}
     *
     * @param entities  DTOs for links.
     * @param offset    offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of DTOs with links.
     */
    public CollectionModel<T> toCollectionModel(Iterable<T> entities,
                                                Integer offset, Map<String, String[]> reqParams) {
        CollectionModel<T> collectionModel = CollectionModel.of(entities);
        int size = collectionModel.getContent().size();
        if (size == 0) {
            return collectionModel;
        }
        UriBuilderResult uriBuilderResult = uriBuilder.buildRequestParams(reqParams);
        addFirstPage(collectionModel, uriBuilderResult);
        if (size < 5) {
            return collectionModel;
        }
        addNextPage(collectionModel, uriBuilderResult);
        return collectionModel;
    }

    protected abstract void addFirstPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult);

    protected abstract void addNextPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult);

    protected abstract void addModelLinks(T dto);


}
