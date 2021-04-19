package com.epam.esm.criteria.result;

import com.epam.esm.criteria.Criteria;

/**
 * This class is a container of {@link com.epam.esm.criteria.factory.CriteriaFactory} result. Contains necessary {@link Criteria}
 * and array of parameters for searching entities.
 *
 * @param <T> type of entity.
 * @since 1.0
 */
public class CriteriaFactoryResult<T> {

    private final Criteria<T> criteria;
    private final String[] params;

    public CriteriaFactoryResult(Criteria<T> criteria) {
        this.criteria = criteria;
        this.params = null;
    }


    public CriteriaFactoryResult(Criteria<T> criteria, String[] params) {
        this.criteria = criteria;
        this.params = params;
    }

    public Criteria<T> getCriteria() {
        return criteria;
    }

    public String[] getParams() {
        return params;
    }

}
