package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Abstract QueryBuilder which uses template method with common logic.
 *
 * @since 2.0
 */
public abstract class AbstractQueryBuilder<T> {

    protected final EntityManager entityManager;

    @Autowired
    protected AbstractQueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * This method constructs query for entity according to given request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return query for executing.
     * @since 2.0
     */
    public TypedQuery<T> buildQuery(Map<String, String[]> reqParams, int limit, int offset) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getGenericClass());
        Root<T> root = criteriaQuery.from(getGenericClass());

        //where
        List<Predicate> wherePredicates = getWherePredicates(reqParams, criteriaBuilder, root);
        wherePredicates.stream()
                .reduce(criteriaBuilder::or)
                .ifPresent(criteriaQuery::where);
        //sort
        String[] params;
        params = reqParams.get(ApplicationConstants.SORT_FIELDS_KEY);
        if (params != null) {
            String[] order = reqParams.get(ApplicationConstants.ORDER_KEY);
            if (order != null && ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order[0])) {
                Stream.of(params)
                        .forEach(param -> setOrder(param, ApplicationConstants.DESC_ORDER, criteriaQuery, root, criteriaBuilder));
            } else {
                Stream.of(params)
                        .forEach(param -> setOrder(param, ApplicationConstants.ASC_ORDER, criteriaQuery, root, criteriaBuilder));
            }
        }

        //limit&offset
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return query;
    }

    /**
     * This method gets predicate for equals operation.
     *
     * @param params    all passed params for equals.
     * @param fieldName the field with which to compare.
     * @return reduced predicate with equals operations.
     * @since 2.0
     */
    protected Predicate getEqualsPredicate(Object[] params, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return Stream.of(params)
                .map(param -> criteriaBuilder.equal(root.get(fieldName), param))
                .reduce(criteriaBuilder::or).orElse(null);
    }

    /**
     * This method gets predicate for like operation.
     *
     * @param params    all passed params for like.
     * @param fieldName the field with which to compare.
     * @return reduced predicate with like operations.
     * @since 2.0
     */
    protected Predicate getLikePredicate(String[] params, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return Stream.of(params)
                .map(param -> criteriaBuilder.like(root.get(fieldName), "%" + param + "%"))
                .reduce(criteriaBuilder::or).orElse(null);
    }

    /**
     * This method gets predicate for where operation.
     *
     * @param reqParams all passed request params.
     * @return list of all predicates for where operation.
     * @since 2.0
     */
    protected abstract List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<T> root);

    /**
     * This method gets predicate for equal operation with joining between two tables.
     * All predicates reduces as OR statement.
     *
     * @param params        all passed params for join.
     * @param attributeName attribute of entity for joining.
     * @param fieldName     the field with which to compare.
     * @return reduced predicate with equal operations.
     * @since 2.0
     */
    protected Predicate getJoinPredicate(String[] params, String attributeName, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return Stream.of(params)
                .map(param -> {
                    Join<?, ?> join = root.join(attributeName);
                    return criteriaBuilder.equal(join.get(fieldName), param);

                }).reduce(criteriaBuilder::or).orElse(null);
    }

    /**
     * This method sets an order for result set.
     *
     * @param field which field to sort by.
     * @param order sort order
     * @param query current constructed query.
     * @since 2.0
     */
    protected abstract void setOrder(String field, String order, CriteriaQuery<T> query, Root<T> root,
                                     CriteriaBuilder criteriaBuilder);

    /**
     * This method gets a class of entity.
     *
     * @return class of entity.
     */
    protected abstract Class<T> getGenericClass();


}
