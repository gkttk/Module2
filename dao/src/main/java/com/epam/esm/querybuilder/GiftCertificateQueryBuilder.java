package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.querybuilder.parameterparser.ParameterParser;
import com.epam.esm.querybuilder.parameterparser.enums.Operators;
import com.epam.esm.querybuilder.parameterparser.parserresult.ParserResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Implementation of {@link com.epam.esm.querybuilder.QueryBuilder} interface
 * and {@link com.epam.esm.querybuilder.AbstractQueryBuilder} class.
 *
 * @since 2.0
 */
@Component
public class GiftCertificateQueryBuilder extends AbstractQueryBuilder<GiftCertificate> implements QueryBuilder<GiftCertificate> {

    private final ParameterParser parameterParser;


    @Autowired
    public GiftCertificateQueryBuilder(EntityManager entityManager, ParameterParser parameterParser) {
        super(entityManager);
        this.parameterParser = parameterParser;
    }

    /**
     * This method gets predicate for equal operation with joining.
     * All predicates reduces as AND statement.
     *
     * @param params        all passed params for join.
     * @param attributeName attribute of entity for joining.
     * @param fieldName     the field with which to compare.
     * @return reduced predicate with equal operations.
     * @since 2.0
     */
    private Predicate getJoinAndPredicate(String[] params, String attributeName, String fieldName, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        return Stream.of(params)
                .map(param -> {
                    Join<GiftCertificate, Tag> join = root.join(attributeName);
                    return criteriaBuilder.equal(join.get(fieldName), param);

                }).reduce(criteriaBuilder::and).orElse(null);
    }

    /**
     * {@link AbstractQueryBuilder#getGenericClass()}
     */
    @Override
    protected Class<GiftCertificate> getGenericClass() {
        return GiftCertificate.class;
    }

    /**
     * {@link AbstractQueryBuilder#getWherePredicates(Map, CriteriaBuilder, Root)} ()}
     */
    @Override
    protected List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] params;
        params = reqParams.get(ApplicationConstants.NAMES_PART_KEY);
        if (params != null) {
            Predicate predicate = getLikePredicate(params, ApplicationConstants.NAME_FIELD, criteriaBuilder, root);
            predicates.add(criteriaBuilder.or(predicate));
        }
        params = reqParams.get(ApplicationConstants.DESCRIPTION_PART_KEY);
        if (params != null) {
            Predicate predicate = getLikePredicate(params, ApplicationConstants.DESCRIPTION_FIELD, criteriaBuilder, root);
            criteriaBuilder.or(predicate);
            predicates.add(criteriaBuilder.or(predicate));
        }
        params = reqParams.get(ApplicationConstants.TAG_NAMES_KEY);
        if (params != null) {
            for (String param : params) {
                ParserResult parserResult = parameterParser.parseRequestParameter(param);
                Operators operator = parserResult.getOperator();
                String[] tagNames = parserResult.getParameters();
                Predicate predicate;
                if (ApplicationConstants.AND_OPERATOR_NAME.equals(operator.name())) {
                    predicate = getJoinAndPredicate(tagNames, ApplicationConstants.TAGS_ATTRIBUTE_NAME,
                            ApplicationConstants.TAG_NAME_FIELD, criteriaBuilder, root);
                    criteriaBuilder.and(predicate);
                } else {
                    predicate = getJoinPredicate(tagNames, ApplicationConstants.TAGS_ATTRIBUTE_NAME,
                            ApplicationConstants.TAG_NAME_FIELD, criteriaBuilder, root);
                    criteriaBuilder.or(predicate);
                }
                predicates.add(criteriaBuilder.or(predicate));
            }
        }
        params = reqParams.get(ApplicationConstants.TAG_AND_NAMES_KEY);
        if (params != null) {
            Predicate predicate = getJoinAndPredicate(params, ApplicationConstants.TAGS_ATTRIBUTE_NAME,
                    ApplicationConstants.TAG_NAME_FIELD, criteriaBuilder, root);
            criteriaBuilder.and(predicate);
            predicates.add(criteriaBuilder.or(predicate));
        }

        return predicates;
    }

    /**
     * {@link AbstractQueryBuilder#setOrder(String, String, CriteriaQuery, Root, CriteriaBuilder)} ()}
     */
    @Override
    protected void setOrder(String field, String order, CriteriaQuery<GiftCertificate> query, Root<GiftCertificate> root,
                            CriteriaBuilder criteriaBuilder) {
        boolean isDesc = ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order);
        switch (field) {
            case ApplicationConstants.ID_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.ID_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.ID_FIELD)));
                }
                break;
            }
            case ApplicationConstants.NAME_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.NAME_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.NAME_FIELD)));
                }
                break;
            }
            case ApplicationConstants.DESCRIPTION_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.DESCRIPTION_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.DESCRIPTION_FIELD)));
                }
                break;
            }
            case ApplicationConstants.PRICE_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.PRICE_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.PRICE_FIELD)));
                }
                break;
            }
            case ApplicationConstants.DURATION_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.DURATION_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.DURATION_FIELD)));
                }
                break;
            }
            case ApplicationConstants.CREATE_DATE_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.CREATE_DATE_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.CREATE_DATE_FIELD)));
                }
                break;
            }
            case ApplicationConstants.LAST_UPDATE_DATE_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.LAST_UPDATE_DATE_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.LAST_UPDATE_DATE_FIELD)));
                }
                break;
            }
        }
    }


}
