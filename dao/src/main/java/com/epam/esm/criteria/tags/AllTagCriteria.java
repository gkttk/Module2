package com.epam.esm.criteria.tags;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.AbstractCriteria;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This Criteria gets all GiftCertificate entities in DB.
 *
 * @since 1.0
 */
@Component("allTagCriteria")
public class AllTagCriteria extends AbstractCriteria<Tag> implements Criteria<Tag> {

    @Autowired
    public AllTagCriteria(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<Tag> find(String[] params, int limit, int offset) {
        return entityManager.createQuery(ApplicationConstants.GET_ALL_TAG_QUERY, Tag.class)
                .setMaxResults(limit).setFirstResult(offset).getResultList();
    }

}
