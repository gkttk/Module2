package com.epam.esm.criteria;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


/**
 * Abstract Criteria class with common fields.
 *
 * @since 1.0
 */
public abstract class AbstractCriteria<T> implements Criteria<T> {

    protected final JdbcTemplate template;
    protected final RowMapper<T> rowMapper;

    protected AbstractCriteria(JdbcTemplate template, RowMapper<T> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

}
