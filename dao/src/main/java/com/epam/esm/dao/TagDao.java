package com.epam.esm.dao;

import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents an api to interact with the Tag entity in database.
 * Implementations : {@link com.epam.esm.dao.impl.TagDaoImpl} classes.
 *
 * @since 1.0
 */
public interface TagDao {

    /**
     * This method get a number of entity in the db.
     * @return number of Tag entity in DB.
     * @since 2.0
     */
    long count();

    /**
     * Find Tag the most widely used tags of user with given id.
     *
     * @return List with Tags entity.
     * @since 2.0
     */
    List<Tag> findMaxWidelyUsed(long userId);


    /**
     * This method combines all getList queries.
     *
     * @param reqParams is a map of all request parameters.
     * @param limit     for pagination
     * @param offset    for pagination
     * @return list of Tag entities
     * @since 1.0
     */
    List<Tag> findBy(Map<String, String[]> reqParams, int limit, int offset);

    /**
     * This method saves Tag entity.
     *
     * @param tag Tag entity without id.
     * @return Saved Tag entity.
     * @since 1.0
     */
    Tag save(Tag tag);

    /**
     * This method get Tag entity by id.
     *
     * @param id Tag entity's id.
     * @return Optional of Tag entity.If there is no Tag with given id, return Optional.empty().
     * @since 1.0
     */
    Optional<Tag> findById(long id);

    /**
     * This method get Tag entity by name.
     *
     * @param tagName Tag entity's name.
     * @return Optional of Tag entity.If there is no Tag with given name, return Optional.empty().
     * @since 1.0
     */
    Optional<Tag> findByName(String tagName);

    /**
     * This method delete Tag entity.
     *
     * @param id Tag entity id.
     * @return a boolean which shows if Tag entity with given id was in db.
     * @since 1.0
     */
    boolean deleteById(long id);


}
