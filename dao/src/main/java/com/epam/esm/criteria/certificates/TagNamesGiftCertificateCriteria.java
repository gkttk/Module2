package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * This Criteria gets GiftCertificate entities by their tag names.
 *
 * @since 1.0
 */
@Component("tagNamesGCCriteria")
public class TagNamesGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    public TagNamesGiftCertificateCriteria(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GiftCertificate> find(String[] params) {
        Set<GiftCertificate> result = new HashSet<>();
        Arrays.stream(params).forEach(name -> {
            List<GiftCertificate> certificates = entityManager.createQuery(ApplicationConstants.FIND_ALL_GC_BY_TAG_NAMES, GiftCertificate.class)
                    .setParameter(ApplicationConstants.TAG_NAME_FIELD, name)
                    .getResultList();
            result.addAll(certificates);
        });

        return new ArrayList<>(result);
    }

}
