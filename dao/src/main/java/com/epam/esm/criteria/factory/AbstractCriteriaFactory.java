package com.epam.esm.criteria.factory;

import org.springframework.context.ApplicationContext;


/**
 * Abstract class for all CriteriaFactories with common fields.
 *
 * @since 1.0
 */
public abstract class AbstractCriteriaFactory {

    private ApplicationContext appContext;

    protected ApplicationContext getAppContext() {
        return appContext;
    }

    public AbstractCriteriaFactory(ApplicationContext appContext) {
        this.appContext = appContext;
    }
}
