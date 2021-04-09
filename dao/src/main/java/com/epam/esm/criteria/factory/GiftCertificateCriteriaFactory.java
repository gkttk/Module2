package com.epam.esm.criteria.factory;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class return {@link CriteriaFactoryResult} which contains necessary {@link com.epam.esm.criteria.Criteria} and
 * parameters for searching.
 *
 * @since 1.0
 */
@Component
public class GiftCertificateCriteriaFactory implements CriteriaFactory<GiftCertificate> {

    private final Criteria<GiftCertificate> namesPartCriteria;
    private final Criteria<GiftCertificate> descriptionPartCriteria;
    private final Criteria<GiftCertificate> tagNamesCriteria;
    private final Criteria<GiftCertificate> allGCCriteria;

    @Autowired
    public GiftCertificateCriteriaFactory(@Qualifier(ApplicationConstants.NAMES_PART_CRITERIA_BEAN_NAME) Criteria<GiftCertificate> namesPartCriteria,
                                          @Qualifier(ApplicationConstants.DESCRIPTION_PART_CRITERIA_BEAN_NAME) Criteria<GiftCertificate> descriptionPartCriteria,
                                          @Qualifier(ApplicationConstants.TAG_NAMES_CRITERIA_BEAN_NAME) Criteria<GiftCertificate> tagNamesCriteria,
                                          @Qualifier(ApplicationConstants.ALL_CRITERIA_BEAN_NAME) Criteria<GiftCertificate> allGCCriteria) {
        this.namesPartCriteria = namesPartCriteria;
        this.descriptionPartCriteria = descriptionPartCriteria;
        this.tagNamesCriteria = tagNamesCriteria;
        this.allGCCriteria = allGCCriteria;
    }

    public CriteriaFactoryResult<GiftCertificate> getCriteriaWithParams(Map<String, String[]> reqParams) {
        String[] params;
        params = reqParams.get(ApplicationConstants.NAMES_PART_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>(namesPartCriteria, params);
        }
        params = reqParams.get(ApplicationConstants.DESCRIPTION_PART_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>(descriptionPartCriteria, params);
        }
        params = reqParams.get(ApplicationConstants.TAG_NAMES_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>(tagNamesCriteria, params);
        }
        return new CriteriaFactoryResult<>(allGCCriteria);
    }


}