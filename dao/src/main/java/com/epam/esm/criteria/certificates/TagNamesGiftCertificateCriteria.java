package com.epam.esm.criteria.certificates;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This Criteria gets GiftCertificate entities by their tag names.
 *
 * @since 1.0
 */
@Component("tagNamesGCCriteria")
public class TagNamesGiftCertificateCriteria extends AbstractGiftCertificateCriteria implements Criteria<GiftCertificate> {

    private final static String GET_ALL_BY_TAG_NAME = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration," +
            " create_date, last_update_date FROM " + TABLE_NAME + " gc JOIN certificates_tags ct on gc.id = ct.certificate_id"
            + " JOIN tag t on t.id = ct.tag_id WHERE t.name = ?";


    public TagNamesGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(name -> {
            List<GiftCertificate> query = template.query(GET_ALL_BY_TAG_NAME, rowMapper, name);
            result.addAll(query);
        });
        return new ArrayList<>(result);
    }

}
