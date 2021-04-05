package com.epam.esm.criteria.factory;

import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.criteria.tags.AllTagCriteria;
import com.epam.esm.criteria.tags.CertificateIdTagCriteria;
import com.epam.esm.entity.Tag;
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
public class TagCriteriaFactory extends AbstractCriteriaFactory implements CriteriaFactory<Tag> {
    private final static String ALL_CRITERIA_TAG_BEAN_NAME = "allTagCriteria";
    private final static String CERTIFICATE_ID_TAG_BEAN_NAME = "certificateIdTagCriteria";
    private final static String CERTIFICATE_ID_KEY = "certificateId";

    @Autowired
    public TagCriteriaFactory(ApplicationContext appContext) {
        super(appContext);
    }

    public CriteriaFactoryResult<Tag> getCriteriaWithParams(Map<String, String[]> reqParams) {
        String[] params;
        params = reqParams.get(CERTIFICATE_ID_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>((CertificateIdTagCriteria) getAppContext().getBean(CERTIFICATE_ID_TAG_BEAN_NAME),
                    params);
        }

        return new CriteriaFactoryResult<>((AllTagCriteria) getAppContext().getBean(ALL_CRITERIA_TAG_BEAN_NAME));
    }


}