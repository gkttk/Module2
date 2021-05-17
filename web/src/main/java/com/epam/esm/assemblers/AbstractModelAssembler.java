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
     * {@link com.epam.esm.assemblers.ModelAssembler#toCollectionModel(Iterable, Integer, long, Map)}
     *
     * @param entities  DTOs for links.
     * @param offset    offset for pagination.
     * @param reqParams parameters of current request.
     * @return list of DTOs with links.
     */
    public CollectionModel<T> toCollectionModel(Iterable<T> entities,
                                                Integer offset, long count, Map<String, String[]> reqParams) {
        CollectionModel<T> collectionModel = CollectionModel.of(entities);
        int size = collectionModel.getContent().size();
        if (size == 0) {
            return collectionModel;
        }
        UriBuilderResult uriBuilderResult = uriBuilder.buildRequestParams(reqParams);
        addFirstPage(collectionModel, uriBuilderResult);

        int delta = (int) count - offset;
        int limit = uriBuilderResult.getLimit();
        if (limit < delta) {
            addNextPage(collectionModel, uriBuilderResult);
        }

        addLastPage(collectionModel, uriBuilderResult, count);

        return collectionModel;
    }

    protected abstract void addFirstPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult);

    protected abstract void addNextPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult);

    protected abstract void addLastPage(CollectionModel<T> collectionModel, UriBuilderResult uriBuilderResult, long count);

    protected abstract void addModelLinks(T dto);


}
