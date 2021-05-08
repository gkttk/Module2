package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificate_;
import com.epam.esm.entity.Order_;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.Tag_;
import com.epam.esm.entity.User;
import com.epam.esm.entity.User_;
import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.dao.TagDao} interface.
 *
 * @since 1.0
 */
@Repository
public class TagDaoImpl implements TagDao {

    private final EntityManager entityManager;
    private final QueryBuilder<Tag> queryBuilder;

    @Autowired
    public TagDaoImpl(EntityManager entityManager, QueryBuilder<Tag> queryBuilder) {
        this.entityManager = entityManager;
        this.queryBuilder = queryBuilder;
    }

    /**
     * This method saves Tag entity.
     *
     * @param tag Tag entity without id.
     * @return Saved Tag entity.
     * @since 1.0
     */
    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        entityManager.detach(tag);
        return tag;
    }

    /**
     * This method get Tag entity by id.
     *
     * @param id Tag entity's id.
     * @return Optional of Tag entity.If there is no Tag with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> findById(long id) {
        Tag result = entityManager.find(Tag.class, id);
        if (result != null) {
            entityManager.detach(result);
            return Optional.of(result);
        }

        return Optional.empty();
    }

    /**
     * This method get Tag entity by name.
     *
     * @param tagName Tag entity's name.
     * @return Optional of Tag entity.If there is no Tag with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<Tag> findByName(String tagName) {
        TypedQuery<Tag> query = entityManager.createQuery(ApplicationConstants.GET_TAG_BY_NAME, Tag.class)
                .setParameter(ApplicationConstants.TAG_NAME_FIELD, tagName);

        Optional<Tag> tagOpt = query.getResultStream().findFirst();
        tagOpt.ifPresent(entityManager::detach);

        return tagOpt;
    }


    /**
     * Find Tag the most widely used tags of user with given id.
     *
     * @return Optional with Tag entities.
     * @since 2.0
     */
    @Override
    public List<Tag> findMaxWidelyUsed(long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        //start of subSelect
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);

        ListJoin<User, com.epam.esm.entity.Order> countJoin1 = countRoot.join(User_.orders);
        ListJoin<com.epam.esm.entity.Order, GiftCertificate> countJoin2 = countJoin1.join(Order_.giftCertificates);
        ListJoin<GiftCertificate, Tag> countJoin3 = countJoin2.join(GiftCertificate_.tags);

        countQuery.where(cb.equal(countRoot.get(User_.id), userId));
        countQuery.groupBy(countJoin3.get(Tag_.id));
        countQuery.orderBy(cb.desc(cb.count(countJoin3.get(Tag_.id))));
        countQuery.select(cb.count(countJoin3.get(Tag_.id)));

        Optional<Long> countStream = entityManager.createQuery(countQuery)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
        
        if (!countStream.isPresent()){
            return Collections.emptyList();
        }
        Long countResult = countStream.get();
        //end of subSelect

        //start of mainSelect
        CriteriaQuery<Tag> query = cb.createQuery(Tag.class);
        Root<User> root = query.from(User.class);
        ListJoin<User, com.epam.esm.entity.Order> join1 = root.join(User_.orders);
        ListJoin<com.epam.esm.entity.Order, GiftCertificate> join2 = join1.join(Order_.giftCertificates);
        ListJoin<GiftCertificate, Tag> join3 = join2.join(GiftCertificate_.tags);

        query.where(cb.equal(root.get(User_.id), userId));
        query.groupBy(join3.get(Tag_.id));

        query.having(cb.equal(cb.count(join3.get(Tag_.id)), countResult));
        query.select(join3);
        //end of mainSelect

        return entityManager.createQuery(query)
                .getResultStream()
                .peek(entityManager::detach)
                .collect(Collectors.toList());
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of Tag entities
     * @since 1.0
     */
    @Override
    public List<Tag> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<Tag> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList()
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if Tag entity with given id was in db.
     * @since 1.0
     */
    @Override
    public boolean deleteById(long id) {
        Tag reference = entityManager.find(Tag.class, id);
        if (reference != null) {
            entityManager.remove(reference);
            return true;
        }
        return false;
    }


}
