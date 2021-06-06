package com.epam.esm.dao.domain;

import com.epam.esm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface represents an api to interact with the User entity in database.
 *
 * @since 4.0
 */
public interface UserDao extends JpaRepository<User, Long> {

    /**
     * This method get User entity by login.
     *
     * @param login User entity's login.
     * @return Optional of User entity. If there is no User with given name, return Optional.empty().
     * @since 1.0
     */
    Optional<User> findByLogin(String login);

    /**
     * This method get User entity by login and password.
     *
     * @param login User entity's login.
     * @return Optional of User entity. If there is no User with given name, return Optional.empty().
     * @since 4.0
     */
    Optional<User> findByLoginAndPassword(String login, String password);
}
