package com.epam.esm.dao.domain.impl;

import com.epam.esm.dao.domain.CriteriaFindAllDao;
import com.epam.esm.querybuilder.QueryBuilder;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCriteriaFindAllDao<T> implements CriteriaFindAllDao<T> {

    private final QueryBuilder<T> queryBuilder;

    protected AbstractCriteriaFindAllDao(QueryBuilder<T> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of entities
     * @since 4.0
     */
    @Override
    public List<T> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<T> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

}
