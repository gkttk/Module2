package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.querybuilder.parameterparser.ParameterParser;
import com.epam.esm.querybuilder.parameterparser.enums.Operators;
import com.epam.esm.querybuilder.parameterparser.parserresult.ParserResult;

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
    protected final ParameterParser parser;

    protected AbstractQueryBuilder(EntityManager entityManager, ParameterParser parser) {
        this.entityManager = entityManager;
        this.parser = parser;
    }

    /**
     * This method parses parameters and constructs LIKE statement according to passed operator.
     *
     * @param predicates all predicates of current creating query.
     * @param params     parameters of request
     * @param field      field for Like query.
     * @since 2.0
     */
    protected void likeProcess(CriteriaBuilder criteriaBuilder, Root<T> root, List<Predicate> predicates, String[] params,
                               String field) {
        Stream.of(params).forEach(param -> {
            ParserResult parserResult = parser.parseRequestParameter(param);
            Operators operator = parserResult.getOperator();
            String[] values = parserResult.getParameters();
            if (ApplicationConstants.AND_OPERATOR_NAME.equals(operator.name())) {
                Stream.of(values)
                        .map(value -> getLikePredicate(value, field, criteriaBuilder, root))
                        .reduce(criteriaBuilder::and).ifPresent(predicates::add);
            } else {
                Stream.of(values)
                        .map(value -> getLikePredicate(value, field, criteriaBuilder, root))
                        .reduce(criteriaBuilder::or).ifPresent(predicates::add);
            }
        });
    }


    /**
     * This method parses parameters and constructs JOIN statement according to passed operator.
     *
     * @param predicates    all predicates of current creating query.
     * @param params        parameters of request
     * @param field         field for JOIN query.
     * @param attributeName field for JOIN query.
     * @since 2.0
     */
    protected void joinProcess(CriteriaBuilder criteriaBuilder, Root<T> root, List<Predicate> predicates, String[] params,
                               String field, String attributeName) {
        Stream.of(params).forEach(param -> {
            ParserResult parserResult = parser.parseRequestParameter(param);
            Operators operator = parserResult.getOperator();
            String[] values = parserResult.getParameters();

            if (ApplicationConstants.AND_OPERATOR_NAME.equals(operator.name())) {
                Stream.of(values)
                        .map(value -> getJoinPredicate(value, field, attributeName, criteriaBuilder, root))
                        .reduce(criteriaBuilder::and).ifPresent(predicates::add);
            } else {
                Stream.of(values)
                        .map(value -> getJoinPredicate(value, field, attributeName, criteriaBuilder, root))
                        .reduce(criteriaBuilder::or).ifPresent(predicates::add);
            }
        });
    }

    /**
     * This method parses parameters and constructs EQUAL statement according to passed operator.
     *
     * @param predicates all predicates of current creating query.
     * @param params     parameters of request
     * @param field      field for EQUAL query.
     * @since 2.0
     */
    protected void equalProcess(CriteriaBuilder criteriaBuilder, Root<T> root, List<Predicate> predicates, String[] params,
                                String field) {
        Stream.of(params).forEach(param -> {
            ParserResult parserResult = parser.parseRequestParameter(param);
            Operators operator = parserResult.getOperator();
            String[] values = parserResult.getParameters();
            if (ApplicationConstants.AND_OPERATOR_NAME.equals(operator.name())) {
                Stream.of(values)
                        .map(value -> getEqualsPredicate(value, field, criteriaBuilder, root))
                        .reduce(criteriaBuilder::and).ifPresent(predicates::add);
            } else {
                Stream.of(values)
                        .map(value -> getEqualsPredicate(value, field, criteriaBuilder, root))
                        .reduce(criteriaBuilder::or).ifPresent(predicates::add);
            }
        });
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
            String[] orderValues = reqParams.get(ApplicationConstants.ORDER_KEY);
            String order = orderValues[0];
            if (order != null && ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order.trim())) {
                Stream.of(params)
                        .forEach(param -> setOrder(param, ApplicationConstants.DESC_ORDER, criteriaQuery, root, criteriaBuilder));
            } else {
                Stream.of(params)
                        .forEach(param -> setOrder(param, ApplicationConstants.ASC_ORDER, criteriaQuery, root, criteriaBuilder));
            }
        }

        //limit&offset
        criteriaQuery.distinct(true);
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        query.setMaxResults(limit);
        query.setFirstResult(offset);

        return query;
    }

    /**
     * This method gets predicate for equals operation.
     *
     * @param param     passed param for equals.
     * @param fieldName the field with which to compare.
     * @return reduced predicate with equals operations.
     * @since 2.0
     */
    protected Predicate getEqualsPredicate(String param, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.equal(root.get(fieldName), param);
    }

    /**
     * This method gets predicate for like operation.
     *
     * @param param     passed param for like.
     * @param fieldName the field with which to compare.
     * @return reduced predicate with like operations.
     * @since 2.0
     */
    protected Predicate getLikePredicate(String param, String fieldName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        return criteriaBuilder.like(root.get(fieldName), "%" + param + "%");

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
     * @param param         passed param for join.
     * @param attributeName attribute of entity for joining.
     * @param fieldName     the field with which to compare.
     * @return reduced predicate with equal operations.
     * @since 2.0
     */
    protected Predicate getJoinPredicate(String param, String fieldName, String attributeName, CriteriaBuilder criteriaBuilder, Root<T> root) {
        Join<?, ?> join = root.join(attributeName);
        return criteriaBuilder.equal(join.get(fieldName), param);
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
