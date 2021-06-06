package com.epam.esm.dao.domain;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents an api to interact with the Tag entity in database.
 *
 * @since 4.0
 */
public interface TagDao extends JpaRepository<Tag, Long> {

    /**
     * This method get Tag entity by name.
     *
     * @param name Tag entity's name.
     * @return Optional of Tag entity.If there is no Tag with given name, return Optional.empty().
     * @since 4.0
     */
    Optional<Tag> findByName(String name);

    /**
     * Find Tag the most widely used tags of user with given id.
     *
     * @return List with Tags entity.
     * @since 2.0
     */
    @Query(value = ApplicationConstants.FIND_MAX_WIDELY_USED_QUERY, nativeQuery = true)
    List<Tag> findMaxWidelyUsed(@Param("userId") long userId);
}
