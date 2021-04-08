package com.epam.esm.criteria.tags;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This Criteria gets Tag entities by their certificate id.
 *
 * @since 1.0
 */
@Component("certificateIdTagCriteria")
public class CertificateIdTagCriteria extends AbstractCriteria<Tag> implements Criteria<Tag> {

    @Autowired
    public CertificateIdTagCriteria(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<Tag> find(String[] params) {
        Set<Tag> result = new HashSet<>();
        Arrays.stream(params).forEach(certId -> {
            List<Tag> query = template.query(ApplicationConstants.GET_ALL_TAG_BY_CERTIFICATE_ID, rowMapper, certId);
            result.addAll(query);
        });
        return new ArrayList<>(result);
    }

}
