package com.epam.esm.security.token.parser;

import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.Set;

/**
 * This interface is responsible for getting data from token.
 *
 * @since 5.0
 */
public interface TokenParser {
    /**
     * This method returns expiration date from given token with given secret.
     *
     * @param token         from which to get the date.
     * @param currentSecret secret for the token.
     * @return expiration date of the given token.
     */
    Date getExpirationFromToken(String token, String currentSecret);

    /**
     * This method returns user name from given token with given secret.
     *
     * @param token         from which to get the user name.
     * @param currentSecret secret for the token.
     * @return user name of the given token.
     */
    String getUserNameFromToken(String token, String currentSecret);

    /**
     * This method returns user id from given token with given secret.
     *
     * @param token         from which to get the user id.
     * @param currentSecret secret for the token.
     * @return user id of the given token.
     */
    long getUserIdFromToken(String token, String currentSecret);

    /**
     * This method returns set of granted authorities from given token with given secret.
     *
     * @param token         from which to get the set of granted authorities.
     * @param currentSecret secret for the token.
     * @return set of granted authorities of the given token.
     */
    Set<GrantedAuthority> getGrantedAuthoritiesFromToken(String token, String currentSecret);

}
