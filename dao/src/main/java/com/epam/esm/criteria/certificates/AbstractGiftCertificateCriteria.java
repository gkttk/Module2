package com.epam.esm.criteria.certificates;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


/**
 * Abstract Criteria class with common fields.
 *
 * @since 1.0
 */
public abstract class AbstractGiftCertificateCriteria implements Criteria<GiftCertificate> {

    protected final static String TABLE_NAME = "gift_certificate";

    protected final JdbcTemplate template;
    protected final RowMapper<GiftCertificate> rowMapper;

    protected AbstractGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }
}
