package com.epam.esm.dao;

import com.epam.esm.criteria.result.CriteriaFactoryResult;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents an api to interact with the Tag entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.TagDaoImpl} classes.
 *
 * @since 1.0
 */
public interface TagDao {

    /**
     * This method combines all getList queries.
     *
     * @param criteriaWithParams an instance of {@link CriteriaFactoryResult} which contains {@link com.epam.esm.criteria.Criteria}
     *                           and arrays of params for searching.
     * @return list of Tag entities
     * @since 1.0
     */
    List<Tag> findBy(CriteriaFactoryResult<Tag> criteriaWithParams);

    /**
     * This method saves Tag entity.
     *
     * @param tag Tag entity without id.
     * @return Tag entity with generated id.
     * @since 1.0
     */
    Tag save(Tag tag);

    /**
     * This method get Tag entity by id.
     *
     * @param id Tag entity's id.
     * @return Optional of Tag entity.
     * @since 1.0
     */
    Optional<Tag> findById(long id);

    /**
     * This method get Tag entity by name.
     *
     * @param tagName Tag entity's name.
     * @return Optional of Tag entity.
     * @since 1.0
     */
    Optional<Tag> findByName(String tagName);

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    boolean delete(long id);


}
