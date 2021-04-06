package com.epam.esm.criteria.certificates;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This Criteria gets GiftCertificate entities by their parts of name. This class uses DB procedures.
 *
 * @since 1.0
 */
@Component("namePartsGCCriteria")
public class NamePartsGiftCertificateCriteria extends AbstractGiftCertificateCriteria implements Criteria<GiftCertificate> {

    private final static String GET_BY_NAME_PART_PROCEDURE_NAME = "searchByPartOfName";
    private final static String NAME_IN_PARAM_FOR_PART_OF_NAME = "in_partOfName";
    private final static String RESULTS_SET_KEY = "certificates";

    public NamePartsGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(partOfName -> {
            Map<String, Object> procedureResult = executeProcedure(partOfName);
            result.addAll((List<GiftCertificate>) procedureResult.get(RESULTS_SET_KEY));
        });

        return new ArrayList<>(result);

    }

    private Map<String, Object> executeProcedure(String partOfName) {
        SqlParameterSource params = new MapSqlParameterSource().addValue(NAME_IN_PARAM_FOR_PART_OF_NAME, partOfName);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName(GET_BY_NAME_PART_PROCEDURE_NAME)
                .returningResultSet(RESULTS_SET_KEY, rowMapper);

        return simpleJdbcCall.execute(params);
    }


}
