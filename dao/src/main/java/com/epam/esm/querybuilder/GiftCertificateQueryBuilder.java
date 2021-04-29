package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.querybuilder.parameterparser.ParameterParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link com.epam.esm.querybuilder.QueryBuilder} interface
 * and {@link com.epam.esm.querybuilder.AbstractQueryBuilder} class.
 *
 * @since 2.0
 */
@Component
public class GiftCertificateQueryBuilder extends AbstractQueryBuilder<GiftCertificate> implements QueryBuilder<GiftCertificate> {


    @Autowired
    public GiftCertificateQueryBuilder(EntityManager entityManager, ParameterParser parser) {
        super(entityManager, parser);
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
            //example of the parameter ?namesPart=and:firstNamePart,secondNamePart
            likeProcess(criteriaBuilder, root, predicates, params, ApplicationConstants.NAME_FIELD);

        }
        params = reqParams.get(ApplicationConstants.DESCRIPTION_PART_KEY);
        if (params != null) {
            //example of the parameter ?descriptionsPart=and:firstPart,secondPart
            likeProcess(criteriaBuilder, root, predicates, params, ApplicationConstants.DESCRIPTION_FIELD);
        }
        params = reqParams.get(ApplicationConstants.TAG_NAMES_KEY);
        if (params != null) {
            //example of the parameter ?tagNames=and:tag1,tag2
            joinProcess(criteriaBuilder, root, predicates, params, ApplicationConstants.TAG_NAME_FIELD, ApplicationConstants.TAGS_ATTRIBUTE_NAME);
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
