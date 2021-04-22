package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
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
public class OrderQueryBuilder extends AbstractQueryBuilder<Order> implements QueryBuilder<Order> {

    public OrderQueryBuilder(EntityManager entityManager) {
        super(entityManager);

    }

    @Override
    protected Class<Order> getGenericClass() {
        return Order.class;
    }


    protected List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<Order> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] params;
        params = reqParams.get(ApplicationConstants.USER_ID_KEY);
        if (params != null) {
            Predicate predicate = getJoinPredicate(params, ApplicationConstants.USER_ATTRIBUTE_NAME, ApplicationConstants.USER_ID_FIELD, criteriaBuilder, root);
            predicates.add(criteriaBuilder.or(predicate));
        }

        return predicates;
    }

    private Predicate getJoinPredicate(String[] params, String attributeName, String fieldName, CriteriaBuilder criteriaBuilder, Root<Order> root) {
        return Stream.of(params)
                .map(param -> {
                    Join<Tag, GiftCertificate> join = root.join(attributeName);
                    return criteriaBuilder.equal(join.get(fieldName), param);

                }).reduce(criteriaBuilder::or).orElse(null);
    }


    protected void setOrder(String field, String order, CriteriaQuery<Order> query, Root<Order> root,
                            CriteriaBuilder criteriaBuilder) {
        boolean isDesc = ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order);
        switch (field) {
            case ApplicationConstants.ORDER_ID_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.ORDER_ID_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.ORDER_ID_FIELD)));
                }
                break;
            }
            case ApplicationConstants.ORDER_COST_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.ORDER_COST_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.ORDER_COST_FIELD)));
                }
                break;
            }
            case ApplicationConstants.ORDER_CREATION_DATE_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.ORDER_CREATION_DATE_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.ORDER_CREATION_DATE_FIELD)));
                }
                break;
            }
        }
    }


}
