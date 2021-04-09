package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * This Criteria gets GiftCertificate entities by their parts of name. This class uses DB procedures.
 *
 * @since 1.0
 */
@Component("namePartsGCCriteria")
public class NamePartsGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    public NamePartsGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(partOfName -> {
            Map<String, Object> procedureResult = executeProcedure(partOfName);
            result.addAll((List<GiftCertificate>) procedureResult.get(ApplicationConstants.RESULTS_SET_KEY));
        });

        return new ArrayList<>(result);

    }

    private Map<String, Object> executeProcedure(String partOfName) {
        SqlParameterSource params = new MapSqlParameterSource().addValue(ApplicationConstants.NAME_IN_PARAM_FOR_PART_OF_NAME, partOfName);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName(ApplicationConstants.GET_BY_NAME_PART_PROCEDURE_NAME)
                .returningResultSet(ApplicationConstants.RESULTS_SET_KEY, rowMapper);

        return simpleJdbcCall.execute(params);
    }


}
