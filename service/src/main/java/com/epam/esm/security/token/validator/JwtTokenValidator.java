package com.epam.esm.security.token.validator;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Default implementation of {@link com.epam.esm.security.token.validator.TokenValidator}
 *
 * @since 5.0
 */
@Component
public class JwtTokenValidator implements TokenValidator {

    @Value("${token.secret.access}")
    private String accessSecret;

    @Value("${token.secret.refresh}")
    private String refreshSecret;

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        if (refreshToken != null) {
            return validateTokenProcess(refreshToken, refreshSecret);
        }
        return false;
    }

    @Override
    public boolean validateAccessToken(String accessToken) {
        if (accessToken != null) {
            return validateTokenProcess(accessToken, accessSecret);
        }
        return false;
    }

    /**
     * This private method is the process of common validation for tokens.
     *
     * @param token  token for validation.
     * @param secret secret for validation.
     * @return is the given refresh token valid.
     * @since 4.0
     */
    private boolean validateTokenProcess(String token, String secret) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);

            boolean isTokenExpired = claims.getBody().getExpiration().before(new Date());
            return !isTokenExpired;
        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("JWT access token is expired", ApplicationConstants.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is invalid ", ApplicationConstants.TOKEN_INVALID);
        }
    }

}
