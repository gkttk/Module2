package com.epam.esm.criteria.querybuilder;

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
public class TagQueryBuilder extends AbstractQueryBuilder<Tag> implements QueryBuilder<Tag> {

    public TagQueryBuilder(EntityManager entityManager) {
        super(entityManager);

    }

    @Override
    protected Class<Tag> getGenericClass() {
        return Tag.class;
    }


    protected List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<Tag> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] params;
        params = reqParams.get(ApplicationConstants.CERTIFICATE_ID_KEY);
        if (params != null) {
            Predicate predicate = getJoinPredicate(params, ApplicationConstants.GC_ATTRIBUTE_NAME, ApplicationConstants.ID_FIELD, criteriaBuilder, root);
            predicates.add(criteriaBuilder.or(predicate));
        }

        return predicates;
    }

    private Predicate getJoinPredicate(String[] params, String attributeName, String fieldName, CriteriaBuilder criteriaBuilder, Root<Tag> root) {
        return Stream.of(params)
                .map(param -> {
                    Join<Tag, GiftCertificate> join = root.join(attributeName);
                    return criteriaBuilder.equal(join.get(fieldName), param);  //todo like

                }).reduce(criteriaBuilder::or).orElse(null);
    }


    protected void setOrder(String field, String order, CriteriaQuery<Tag> query, Root<Tag> root,
                            CriteriaBuilder criteriaBuilder) {
        boolean isDesc = ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order);
        switch (field) {
            case ApplicationConstants.TAG_ID_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.TAG_ID_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.TAG_ID_FIELD)));
                }
                break;
            }
            case ApplicationConstants.TAG_NAME_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.TAG_NAME_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.TAG_NAME_FIELD)));
                }
                break;
            }
        }
    }


}
