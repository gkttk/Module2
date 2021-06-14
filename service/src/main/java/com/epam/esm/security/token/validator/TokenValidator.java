package com.epam.esm.security.token.validator;

/**
 * This interface is responsible for validation given tokens.
 *
 * @since 5.0
 */
public interface TokenValidator {

    /**
     * This method validates a given refresh token.
     *
     * @return is the given refresh token valid.
     * @since 5.0
     */
    boolean validateRefreshToken(String refreshToken);

    /**
     * This method validates a given access token.
     *
     * @return is the given access token valid.
     * @since 5.0
     */
    boolean validateAccessToken(String accessToken);
}
