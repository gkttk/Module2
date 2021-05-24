package com.epam.esm.dao.domain;

import java.util.List;
import java.util.Map;

public interface CriteriaFindAllDao<T> {

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of entities
     * @since 4.0
     */
    List<T> findBy(Map<String, String[]> reqParams, int limit, int offset);
}
