package com.epam.esm.criteria.tags;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This Criteria gets Tag entities by their certificate id.
 *
 * @since 1.0
 */
@Component("certificateIdTagCriteria")
public class CertificateIdTagCriteria extends AbstractCriteria<Tag> implements Criteria<Tag> {

    @Autowired
    public CertificateIdTagCriteria(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Tag> find(String[] params) {
        Set<Tag> result = new HashSet<>();

        Arrays.stream(params).forEach(certId -> {
            List<Tag> tags = entityManager.createQuery(ApplicationConstants.GET_ALL_TAG_BY_CERTIFICATE_ID, Tag.class)
                    .setParameter(ApplicationConstants.CERTIFICATE_ID_KEY, Long.valueOf(certId))
                    .getResultList();
            result.addAll(tags);
        });
        return new ArrayList<>(result);
    }

}
