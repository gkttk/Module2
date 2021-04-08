package com.epam.esm.criteria.factory;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.certificates.AllGiftCertificateCriteria;
import com.epam.esm.criteria.certificates.DescriptionPartsGiftCertificateCriteria;
import com.epam.esm.criteria.certificates.NamePartsGiftCertificateCriteria;
import com.epam.esm.criteria.certificates.TagNamesGiftCertificateCriteria;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This class return {@link CriteriaFactoryResult} which contains necessary {@link com.epam.esm.criteria.Criteria} and
 * parameters for searching.
 *
 * @since 1.0
 */
@Component
public class GiftCertificateCriteriaFactory extends AbstractCriteriaFactory implements CriteriaFactory<GiftCertificate> {

    @Autowired
    public GiftCertificateCriteriaFactory(ApplicationContext appContext) {
        super(appContext);
    }

    public CriteriaFactoryResult<GiftCertificate> getCriteriaWithParams(Map<String, String[]> reqParams) {
        String[] params;
        params = reqParams.get(ApplicationConstants.NAMES_PART_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>((NamePartsGiftCertificateCriteria) getAppContext().getBean(ApplicationConstants.NAMES_PART_CRITERIA_BEAN_NAME),
                    params);
        }
        params = reqParams.get(ApplicationConstants.DESCRIPTION_PART_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>((DescriptionPartsGiftCertificateCriteria) getAppContext().getBean(ApplicationConstants.DESCRIPTION_PART_CRITERIA_BEAN_NAME),
                    params);
        }
        params = reqParams.get(ApplicationConstants.TAG_NAMES_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>((TagNamesGiftCertificateCriteria) getAppContext().getBean(ApplicationConstants.TAG_NAMES_CRITERIA_BEAN_NAME),
                    params);
        }
        return new CriteriaFactoryResult<>((AllGiftCertificateCriteria) getAppContext().getBean(ApplicationConstants.ALL_CRITERIA_BEAN_NAME));
    }


}