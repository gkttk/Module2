package com.epam.esm.querybuilder;

import javax.persistence.TypedQuery;
import java.util.Map;

/**
 * This interface represents an api to interact with classes that construct query for entities.
 * <p>
 * Implementations :
 * {@link com.epam.esm.querybuilder.GiftCertificateQueryBuilder},
 * {@link com.epam.esm.querybuilder.OrderQueryBuilder},
 * {@link com.epam.esm.querybuilder.TagQueryBuilder},
 * {@link com.epam.esm.querybuilder.UserQueryBuilder}.
 *
 * @param <T> entity class.
 * @since 2.0
 */
public interface QueryBuilder<T> {

    /**
     * This method construct query for entity according to given request parameters.
     *
     * @param reqParams request parameters.
     * @param limit     for pagination.
     * @param offset    for pagination.
     * @return query for executing.
     * @since 2.0
     */
    TypedQuery<T> buildQuery(Map<String, String[]> reqParams, int limit, int offset);
}
