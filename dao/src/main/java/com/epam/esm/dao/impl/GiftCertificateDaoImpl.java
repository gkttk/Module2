package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Default implementation of {@link com.epam.esm.dao.GiftCertificateDao} interface.
 *
 * @since 1.0
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final EntityManager entityManager;
    private final QueryBuilder<GiftCertificate> queryBuilder;

    @Autowired
    public GiftCertificateDaoImpl(EntityManager entityManager, QueryBuilder<GiftCertificate> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams an instance of {@link CriteriaFactoryResult} which contains {@link Criteria}
     *                  and arrays of params for searching.
     * @param limit
     * @param offset
     * @return list of GiftCertificate entities
     * @since 1.0
     */
    public List<GiftCertificate> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<GiftCertificate> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList();
    }


    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> findByName(String name) {
        TypedQuery<GiftCertificate> query = entityManager.createQuery(ApplicationConstants.FIND_GC_BY_NAME_QUERY, GiftCertificate.class)
                .setParameter(ApplicationConstants.NAME_FIELD, name);
        return query.getResultStream().findFirst();
    }

    /**
     * This method saves GiftCertificate entity.
     * The method uses KayHolder for getting generated id for GiftCertificate entity from db.
     *
     * @param certificate GiftCertificate entity without id.
     * @return id of inserted GiftCertificate entity
     * @since 1.0
     */
    @Override
    public GiftCertificate save(GiftCertificate certificate) {
        entityManager.persist(certificate);
        entityManager.flush();
        entityManager.detach(certificate);
        return certificate;

    }

    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> findById(long id) {

        GiftCertificate result = entityManager.find(GiftCertificate.class, id);
        if (result != null){
            entityManager.detach(result);
            return Optional.of(result);
        }

        return Optional.empty();
    }


    /**
     * This method updates all updatable fields for GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity with fields for update.
     * @since 1.0
     */
    @Override
    public GiftCertificate update(GiftCertificate certificate) {

        GiftCertificate reference = entityManager.getReference(GiftCertificate.class, certificate.getId());
        certificate.setCreateDate(reference.getCreateDate());

        GiftCertificate giftCertificate = entityManager.merge(certificate);
        entityManager.flush();
        entityManager.detach(giftCertificate);
        return giftCertificate;
    }

    /**
     * This method delete GiftCertificate entity.
     *
     * @param id GiftCertificate entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate != null) {
            entityManager.remove(giftCertificate);
            return true;
        }
        return false;
    }


}
