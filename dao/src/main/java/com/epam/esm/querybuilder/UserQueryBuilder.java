package com.epam.esm.querybuilder;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.User;
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
public class UserQueryBuilder extends AbstractQueryBuilder<User> implements QueryBuilder<User> {

    public UserQueryBuilder(EntityManager entityManager) {
        super(entityManager);
    }

    /**
     * {@link AbstractQueryBuilder#getGenericClass()}
     */
    @Override
    protected Class<User> getGenericClass() {
        return User.class;
    }

    /**
     * {@link AbstractQueryBuilder#getWherePredicates(Map, CriteriaBuilder, Root)} ()}
     */
    @Override
    protected List<Predicate> getWherePredicates(Map<String, String[]> reqParams, CriteriaBuilder criteriaBuilder, Root<User> root) {
        List<Predicate> predicates = new ArrayList<>();
        String[] params;
        params = reqParams.get(ApplicationConstants.ROLE_KEY);
        if (params != null) {
            Predicate predicate = getEqualsPredicate(params, ApplicationConstants.USER_ROLE_FIELD, criteriaBuilder, root);
            predicates.add(criteriaBuilder.or(predicate));

        }

        return predicates;
    }

    /**
     * {@link AbstractQueryBuilder#setOrder(String, String, CriteriaQuery, Root, CriteriaBuilder)} ()}
     */
    @Override
    protected void setOrder(String field, String order, CriteriaQuery<User> query, Root<User> root,
                            CriteriaBuilder criteriaBuilder) {
        boolean isDesc = ApplicationConstants.DESC_ORDER.equalsIgnoreCase(order);
        switch (field) {
            case ApplicationConstants.USER_ID_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.USER_ID_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.USER_ID_FIELD)));
                }
                break;
            }
            case ApplicationConstants.USER_ROLE_FIELD: {
                if (isDesc) {
                    query.orderBy(criteriaBuilder.desc(root.get(ApplicationConstants.USER_ROLE_FIELD)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(ApplicationConstants.USER_ROLE_FIELD)));
                }
                break;
            }
        }
    }


}
