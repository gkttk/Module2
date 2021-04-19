package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This Criteria gets GiftCertificate entities by their parts of name. This class uses DB procedures.
 *
 * @since 1.0
 */
@Component("namePartsGCCriteria")
public class NamePartsGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    public NamePartsGiftCertificateCriteria(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GiftCertificate> find(String[] params, int limit, int offset) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(partOfDescription -> {
            List<GiftCertificate> resultList = entityManager.createStoredProcedureQuery(ApplicationConstants.GET_BY_NAME_PART_PROCEDURE_NAME, GiftCertificate.class)
                    .registerStoredProcedureParameter(ApplicationConstants.NAME_IN_PARAM_FOR_PART_OF_NAME, String.class, ParameterMode.IN)
                    .setParameter(ApplicationConstants.NAME_IN_PARAM_FOR_PART_OF_NAME, partOfDescription)
                    .getResultList();

            result.addAll(resultList);
        });
        return new ArrayList<>(result);
    }
}
