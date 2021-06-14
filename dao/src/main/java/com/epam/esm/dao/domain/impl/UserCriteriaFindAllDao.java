package com.epam.esm.dao.domain.impl;

import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("userCriteriaFindAllDao")
public class UserCriteriaFindAllDao<User> extends AbstractCriteriaFindAllDao<User> {

    @Autowired
    protected UserCriteriaFindAllDao(@Qualifier("userQueryBuilder") QueryBuilder<User> queryBuilder) {
        super(queryBuilder);
    }
}
