package com.epam.esm.dao.domain.impl;

import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


@Repository("tagCriteriaFindAllDao")
public class TagCriteriaFindAllDao<Tag> extends AbstractCriteriaFindAllDao<Tag> {

    protected TagCriteriaFindAllDao(@Qualifier("tagQueryBuilder") QueryBuilder<Tag> queryBuilder) {
        super(queryBuilder);
    }
}
