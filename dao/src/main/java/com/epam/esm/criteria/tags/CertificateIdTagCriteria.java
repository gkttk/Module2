package com.epam.esm.criteria.tags;

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
public class CertificateIdTagCriteria extends AbstractTagCriteria implements Criteria<Tag> {

    private final static String GET_ALL_BY_CERTIFICATE_ID = "SELECT t.id, t.name FROM " + TABLE_NAME +
            " t JOIN certificates_tags ct on t.id = ct.tag_id WHERE ct.certificate_id = ?";

    @Autowired
    public CertificateIdTagCriteria(JdbcTemplate template, RowMapper<Tag> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<Tag> find(String[] params) {
        Set<Tag> result = new HashSet<>();
        Arrays.stream(params).forEach(certId -> {
            List<Tag> query = template.query(GET_ALL_BY_CERTIFICATE_ID, rowMapper, certId);
            result.addAll(query);
        });
        return new ArrayList<>(result);
    }

}
