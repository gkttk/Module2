package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This Criteria gets GiftCertificate entities by their parts of description. This class uses DB procedures.
 *
 * @since 1.0
 */
@Component("descriptionPartsGCCriteria")
public class DescriptionPartsGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    @Autowired
    public DescriptionPartsGiftCertificateCriteria(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        super(template, rowMapper);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(partOfDescription -> {
            Map<String, Object> procedureResult = executeProcedure(partOfDescription);
            result.addAll((List<GiftCertificate>) procedureResult.get(ApplicationConstants.RESULTS_SET_KEY));
        });

        return new ArrayList<>(result);
    }

    private Map<String, Object> executeProcedure(String partOfDescription) {

        SqlParameterSource params = new MapSqlParameterSource().addValue(ApplicationConstants.NAME_IN_PARAM_FOR_PART_OF_DESCRIPTION, partOfDescription);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName(ApplicationConstants.GET_BY_DESCRIPTION_PART_PROCEDURE_NAME)
                .returningResultSet(ApplicationConstants.RESULTS_SET_KEY, rowMapper);

        return simpleJdbcCall.execute(params);
    }
}
