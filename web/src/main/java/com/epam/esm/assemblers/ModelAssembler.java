package com.epam.esm.assemblers;

import org.springframework.hateoas.CollectionModel;

import java.util.Map;

/**
 * This interface represents of api which helps to enrich result JSON by HATEOAS links.
 *
 * @param <T> class of DTO.
 */
public interface ModelAssembler<T> {

    /**
     * This method add links to single DTO.
     *
     * @param dto DTO for links.
     * @return DTO with links.
     */
    T toModel(T dto);

    /**
     * This method add links to collection of DTOs.
     *
     * @param entities  DTOs for links.
     * @param offset    offset for pagination.
     * @param reqParams parameters of current request.
     * @return collection of DTO with links.
     */
    CollectionModel<T> toCollectionModel(Iterable<T> entities, Integer offset, long count, Map<String, String[]> reqParams, String... urlParts);
}
