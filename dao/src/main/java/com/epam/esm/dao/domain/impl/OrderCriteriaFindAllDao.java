package com.epam.esm.dao.domain.impl;

import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("orderCriteriaFindAllDao")
public class OrderCriteriaFindAllDao<Order> extends AbstractCriteriaFindAllDao<Order> {

    protected OrderCriteriaFindAllDao(@Qualifier("orderQueryBuilder") QueryBuilder<Order> queryBuilder) {
        super(queryBuilder);
    }
}
