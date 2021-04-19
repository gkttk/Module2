package com.epam.esm.criteria.certificates;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This Criteria gets all GiftCertificate entities in DB.
 *
 * @since 1.0
 */
@Component("allGCCriteria")
public class AllGiftCertificateCriteria extends AbstractCriteria<GiftCertificate> implements Criteria<GiftCertificate> {

    @Autowired
    public AllGiftCertificateCriteria(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<GiftCertificate> find(String[] params, int limit, int offset) {
        return entityManager.createQuery(ApplicationConstants.FIND_ALL_GC_QUERY, GiftCertificate.class)
                .setMaxResults(limit).setFirstResult(offset).getResultList();
    }
}
