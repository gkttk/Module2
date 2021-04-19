package com.epam.esm.criteria;

import java.util.List;

/**
 * Common interface for any search criteria.
 *
 * @param <T> type of entity.
 *
 * @since 1.0
 */
public interface Criteria<T> {

    /**
     * This method get entities by given params.
     *
     * @param params parameters for searching.
     * @return list of entities.
     *
     * @since 1.0
     */
    List<T> find(String[] params, int limit, int offset);

}
