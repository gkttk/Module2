package com.epam.esm.criteria.factory;

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

    private final static String NAMES_PART_KEY = "namesPart";
    private final static String DESCRIPTION_PART_KEY = "descriptionsPart";
    private final static String TAG_NAMES_KEY = "tagNames";


    private final static String NAMES_PART_CRITERIA_BEAN_NAME = "namePartsGCCriteria";
    private final static String DESCRIPTION_PART_CRITERIA_BEAN_NAME = "descriptionPartsGCCriteria";
    private final static String TAG_NAMES_CRITERIA_BEAN_NAME = "tagNamesGCCriteria";
    private final static String ALL_CRITERIA_BEAN_NAME = "allGCCriteria";

    @Autowired
    public GiftCertificateCriteriaFactory(ApplicationContext appContext) {
        super(appContext);
    }


    public CriteriaFactoryResult<GiftCertificate> getCriteriaWithParams(Map<String, String[]> reqParams) {

        for (Map.Entry<String, String[]> entry : reqParams.entrySet()) {
            String key = entry.getKey();
            switch (key) {
                case NAMES_PART_KEY: {
                    return new CriteriaFactoryResult<>((NamePartsGiftCertificateCriteria) getAppContext().getBean(NAMES_PART_CRITERIA_BEAN_NAME),
                            entry.getValue());
                }
                case DESCRIPTION_PART_KEY: {
                    return new CriteriaFactoryResult<>((DescriptionPartsGiftCertificateCriteria) getAppContext().getBean(DESCRIPTION_PART_CRITERIA_BEAN_NAME),
                            entry.getValue());
                }
                case TAG_NAMES_KEY: {
                    return new CriteriaFactoryResult<>((TagNamesGiftCertificateCriteria) getAppContext().getBean(TAG_NAMES_CRITERIA_BEAN_NAME),
                            entry.getValue());
                }
            }
        }
        return new CriteriaFactoryResult<>((AllGiftCertificateCriteria) getAppContext().getBean(ALL_CRITERIA_BEAN_NAME));

    }


}