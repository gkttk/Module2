package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This Criteria gets GiftCertificate entities by their tag names.
 *
 * @since 1.0
 */
@Component("tagNamesGCCriteria")
public class TagNamesGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    public TagNamesGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(name -> {
            List<GiftCertificate> query = template.query(ApplicationConstants.GET_ALL_GC_BY_TAG_NAME, rowMapper, name);
            result.addAll(query);
        });
        return new ArrayList<>(result);
    }

}
