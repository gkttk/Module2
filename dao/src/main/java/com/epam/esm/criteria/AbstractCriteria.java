package com.epam.esm.criteria;

import javax.persistence.EntityManager;

/**
 * Abstract Criteria class with common fields.
 *
 * @since 1.0
 */
public abstract class AbstractCriteria<T> implements Criteria<T> {

    protected final EntityManager entityManager;

    protected AbstractCriteria(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
