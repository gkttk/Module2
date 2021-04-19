package com.epam.esm.criteria.querybuilder;

import javax.persistence.TypedQuery;
import java.util.Map;

public interface QueryBuilder<T> {

    TypedQuery<T> buildQuery(Map<String, String[]> reqParams, int limit, int offset);
}
