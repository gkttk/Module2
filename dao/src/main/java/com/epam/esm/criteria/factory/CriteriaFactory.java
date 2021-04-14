package com.epam.esm.criteria.factory;

import com.epam.esm.criteria.result.CriteriaFactoryResult;

import java.util.List;
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
     * This method return a list of instances of {@link CriteriaFactoryResult} which contains {@link com.epam.esm.criteria.Criteria}
     * and array of parameters for searching.
     *
     * @param params request parameters.
     * @return a list of instances of {@link CriteriaFactoryResult}.
     */
     List<CriteriaFactoryResult<T>> getCriteriaWithParams(Map<String, String[]> params);
}
