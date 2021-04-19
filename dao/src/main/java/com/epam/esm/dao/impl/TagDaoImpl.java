package com.epam.esm.dao.impl;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.querybuilder.QueryBuilder;
import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.dao.TagDao;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
     * The method uses KayHolder for getting generated id for Tag entity from db.
     *
     * @param tag Tag entity without id.
     * @return Tag entity with generated id.
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
    public Optional<Tag> findByName(String tagName) {//todo return opt
        TypedQuery<Tag> query = entityManager.createQuery(ApplicationConstants.GET_TAG_BY_NAME, Tag.class);
        Tag tag = query.setParameter(ApplicationConstants.TAG_NAME_FIELD, tagName)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return tag != null ? Optional.of(tag) : Optional.empty();
    }

    /**
     * This method combines all getList queries.
     *
     * @param reqParams an instance of {@link CriteriaFactoryResult} which contains {@link Criteria}
     *                  and arrays of params for searching.
     * @param limit
     * @param offset
     * @return list of Tag entities
     * @since 1.0
     */
    @Override
    public List<Tag> findBy(Map<String, String[]> reqParams, int limit, int offset) {
        TypedQuery<Tag> query = queryBuilder.buildQuery(reqParams, limit, offset);
        return query.getResultList();
    }

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag != null) {
            entityManager.remove(tag);
            return true;
        }
        return false;
    }


}
