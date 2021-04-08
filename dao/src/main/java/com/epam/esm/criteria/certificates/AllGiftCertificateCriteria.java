package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
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
@Component("allGCCriteria")
public class AllGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    @Autowired
    public AllGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        return template.query(ApplicationConstants.GET_ALL_GC_QUERY, rowMapper);
    }
}
