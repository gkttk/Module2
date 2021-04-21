package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
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

@Component
public class GiftCertificateQueryBuilder extends AbstractQueryBuilder<GiftCertificate> implements QueryBuilder<GiftCertificate> {


    public GiftCertificateQueryBuilder(EntityManager entityManager) {
        super(entityManager);

    }

    @Override
    protected Class<GiftCertificate> getGenericClass() {
        return GiftCertificate.class;
    }


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
            Predicate predicate = getJoinPredicate(params, ApplicationConstants.TAGS_ATTRIBUTE_NAME,
                    ApplicationConstants.TAG_NAME_FIELD, criteriaBuilder, root);//todo
            criteriaBuilder.or(predicate);
            predicates.add(criteriaBuilder.or(predicate));
        }

        return predicates;
    }


    private Predicate getJoinPredicate(String[] params, String attributeName, String fieldName, CriteriaBuilder criteriaBuilder, Root<GiftCertificate> root) {
        return Stream.of(params)
                .map(param -> {
                    Join<GiftCertificate, Tag> join = root.join(attributeName);
                    return criteriaBuilder.equal(join.get(fieldName), param);

                }).reduce(criteriaBuilder::or).orElse(null);
    }


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
