package com.epam.esm.criteria.factory;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.entity.Tag;
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
public class TagCriteriaFactory implements CriteriaFactory<Tag> {

    private final Criteria<Tag> certificateIdCriteria;
    private final Criteria<Tag> allTagCriteria;

    @Autowired
    public TagCriteriaFactory(@Qualifier(ApplicationConstants.CERTIFICATE_ID_TAG_BEAN_NAME) Criteria<Tag> certificateIdCriteria,
                              @Qualifier(ApplicationConstants.ALL_CRITERIA_TAG_BEAN_NAME) Criteria<Tag> allTagCriteria) {
        this.certificateIdCriteria = certificateIdCriteria;
        this.allTagCriteria = allTagCriteria;
    }

    public CriteriaFactoryResult<Tag> getCriteriaWithParams(Map<String, String[]> reqParams) {
        String[] params;
        params = reqParams.get(ApplicationConstants.CERTIFICATE_ID_KEY);
        if (params != null) {
            return new CriteriaFactoryResult<>(certificateIdCriteria, params);
        }

        return new CriteriaFactoryResult<>(allTagCriteria);
    }


}