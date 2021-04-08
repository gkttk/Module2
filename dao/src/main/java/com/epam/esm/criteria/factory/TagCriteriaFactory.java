package com.epam.esm.criteria.factory;

import com.epam.esm.constants.ApplicationConstants;
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

    @Autowired
    public TagCriteriaFactory(ApplicationContext appContext) {
        super(appContext);
    }

    public CriteriaFactoryResult<Tag> getCriteriaWithParams(Map<String, String[]> reqParams) {
        String[] params;
        params = reqParams.get(ApplicationConstants.CERTIFICATE_ID_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>((CertificateIdTagCriteria) getAppContext().getBean(ApplicationConstants.CERTIFICATE_ID_TAG_BEAN_NAME),
                    params);
        }

        return new CriteriaFactoryResult<>((AllTagCriteria) getAppContext().getBean(ApplicationConstants.ALL_CRITERIA_TAG_BEAN_NAME));
    }


}