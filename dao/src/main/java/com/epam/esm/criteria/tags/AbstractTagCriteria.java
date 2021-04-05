package com.epam.esm.criteria.tags;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


/**
 * Abstract Criteria class with common fields.
 *
 * @since 1.0
 */
public abstract class AbstractTagCriteria implements Criteria<Tag> {

    protected final static String TABLE_NAME = "tag";

    protected final JdbcTemplate template;
    protected final RowMapper<Tag> rowMapper;

    protected AbstractTagCriteria(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }
}
