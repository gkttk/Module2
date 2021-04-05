package com.epam.esm.criteria.tags;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.certificates.AbstractGiftCertificateCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This Criteria gets all GiftCertificate entities in DB.
 *
 * @since 1.0
 */
@Component("allTagCriteria")
public class AllTagCriteria extends AbstractTagCriteria implements Criteria<Tag> {

    private final static String GET_ALL_QUERY = "SELECT id, name FROM " + TABLE_NAME;

    @Autowired
    public AllTagCriteria(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<Tag> find(String[] params) {
        return template.query(GET_ALL_QUERY, rowMapper);

    }

}
