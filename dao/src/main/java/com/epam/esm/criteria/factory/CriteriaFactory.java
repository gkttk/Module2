package com.epam.esm.criteria.factory;

import com.epam.esm.criteria.result.CriteriaFactoryResult;

import java.util.Map;

/**
 * Common interface for all CriteriaFactories.
 *
 * @param <T> type of entity.
 *
 * @since 1.0
 */
public interface CriteriaFactory<T> {

    /**
     * This method return an instance of {@link CriteriaFactoryResult} which contains {@link com.epam.esm.criteria.Criteria}
     * and array of parameters for searching.
     *
     * @param params request parameters.
     * @return an instance of {@link CriteriaFactoryResult}.
     */
    CriteriaFactoryResult<T> getCriteriaWithParams(Map<String, String[]> params);
}
