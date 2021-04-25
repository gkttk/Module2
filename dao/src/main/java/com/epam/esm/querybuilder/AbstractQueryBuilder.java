package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AbstractQueryBuilder<T>{

    protected final EntityManager entityManager;

    protected AbstractQueryBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    protected  Predicate getEqualsPredicate(Object[] params, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return Stream.of(params)
                .map(param -> criteriaBuilder.equal(root.get(fieldName), param))
                .reduce(criteriaBuilder::or).orElse(null);
    }

    protected Predicate getLikePredicate(String[] params, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return Stream.of(params)
                .map(param -> criteriaBuilder.like(root.get(fieldName), "%" + param + "%"))
                .reduce(criteriaBuilder::or).orElse(null);
    }

    protected abstract Class<T> getGenericClass();

    protected abstract List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<T> root);

    protected abstract void setOrder(String field, String order, CriteriaQuery<T> query, Root<T> root,
                                     CriteriaBuilder criteriaBuilder);


}
