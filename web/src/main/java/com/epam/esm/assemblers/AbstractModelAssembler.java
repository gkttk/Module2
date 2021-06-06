package com.epam.esm.assemblers;

import com.epam.esm.uri_builder.UriBuilder;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;

import java.util.Map;

/**
 * Common abstract class for all ModelAssembler classes.
 *
 * @param <T> class of DTO.
 */
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
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, long, Map, String...)}
     *
     * @param entities  DTOs for links.
     * @param offset    offset for pagination.
     * @param reqParams parameters of current request.
     * @param urlParts parts of current url which were @PathVariable values.
     * @return list of DTOs with links.
     */
    public CollectionModel<T> toCollectionModel(Iterable<T> entities,
                                                Integer offset, long count, Map<String, String[]> reqParams, String...urlParts) {
        CollectionModel<T> collectionModel = CollectionModel.of(entities);
        int size = collectionModel.getContent().size();
        if (size == 0) {
            return collectionModel;
        }
        UriBuilderResult uriBuilderResult = uriBuilder.buildRequestParams(reqParams);
        addFirstPage(collectionModel, uriBuilderResult, urlParts);

        int delta = (int) count - offset;
        int limit = uriBuilderResult.getLimit();
        if (limit < delta) {
            addNextPage(collectionModel, uriBuilderResult, urlParts);
        }

        addLastPage(collectionModel, uriBuilderResult, count, urlParts);

        return collectionModel;
    }

    protected abstract void addFirstPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts);

    protected abstract void addNextPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult, String[] urlParts);

    protected abstract void addLastPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult, long count, String[] urlParts);

    protected abstract void addModelLinks(T dto);
}
