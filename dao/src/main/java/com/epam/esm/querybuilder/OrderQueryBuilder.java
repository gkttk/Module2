package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.Order;
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
public class OrderQueryBuilder extends AbstractQueryBuilder<Order> implements QueryBuilder<Order> {

    @Autowired
    public OrderQueryBuilder(EntityManager entityManager, ParameterParser parser) {
        super(entityManager, parser);
    }

    /**
     * {@link AbstractQueryBuilder#getGenericClass()}
     */
    @Override
    protected Class<Order> getGenericClass() {
        return Order.class;
    }

    /**
     * {@link AbstractQueryBuilder#getWherePredicates(Map, CriteriaBuilder, Root)} ()}
     */
    @Override
    protected List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<Order> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] params;
        params = reqParams.get(ApplicationConstants.USER_ID_KEY);
        if (params != null) {
            //example of the parameter ?userId=or:id1,id2
            joinProcess(criteriaBuilder, root, predicates, params, ApplicationConstants.USER_ID_FIELD, ApplicationConstants.USER_ATTRIBUTE_NAME);
        }

        return predicates;
    }

    /**
     * {@link AbstractQueryBuilder#setOrder(String, String, CriteriaQuery, Root, CriteriaBuilder)} ()}
     */
    @Override
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
