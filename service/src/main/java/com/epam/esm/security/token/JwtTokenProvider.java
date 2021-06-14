package com.epam.esm.security.token;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.domain.dto.token.JwtTokenDto;
import com.epam.esm.domain.dto.token.TokenDto;
import com.epam.esm.security.JwtUserDetails;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import com.epam.esm.security.token.parser.TokenParser;
import com.epam.esm.security.token.validator.TokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class is responsible for access and refresh tokens managing.
 *
 * @since 4.0
 */
@Component
public class JwtTokenProvider {

    private final TokenValidator tokenValidator;
    private final TokenParser tokenParser;

    @Autowired
    public JwtTokenProvider(TokenValidator tokenValidator, TokenParser tokenParser) {
        this.tokenValidator = tokenValidator;
        this.tokenParser = tokenParser;
    }

    /**
     * This method generates a new pair of refresh+access token if passed refreshToken is valid.
     *
     * @param currentRefreshToken refresh token for validation.
     * @return new pair access + refresh token.
     * @since 4.0
     */
    public JwtTokenDto refreshToken(String currentRefreshToken) {
        if (!tokenValidator.validateRefreshToken(currentRefreshToken)) {
            throw new JwtAuthenticationException("Refresh token is expired", ApplicationConstants.REFRESH_TOKEN_EXPIRED);
        }

        long userId = tokenParser.getUserIdFromToken(currentRefreshToken, ApplicationConstants.REFRESH_TOKEN_SECRET);
        String userName = tokenParser.getUserNameFromToken(currentRefreshToken, ApplicationConstants.REFRESH_TOKEN_SECRET);
        Set<GrantedAuthority> authorities = tokenParser.getGrantedAuthoritiesFromToken(currentRefreshToken, ApplicationConstants.REFRESH_TOKEN_SECRET);

        JwtUserDetails user = new JwtUserDetails(userId, userName, null, authorities);

        TokenDto newAccessToken = generateToken(user, ApplicationConstants.ACCESS_TOKEN_SECRET, new Date(new Date().getTime() + ApplicationConstants.ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS));

        Date oldRefreshTokenExpirationTime = tokenParser.getExpirationFromToken(currentRefreshToken, ApplicationConstants.REFRESH_TOKEN_SECRET);

        TokenDto newRefreshToken = generateToken(user, ApplicationConstants.REFRESH_TOKEN_SECRET, oldRefreshTokenExpirationTime);
        return new JwtTokenDto(newAccessToken.getToken(), newRefreshToken.getToken(), newAccessToken.getExpiredTime());
    }

    /**
     * This method create a token pair(access+refresh) with information from Authentication instance.
     * Called on the first attempt to authenticate a user without a token.
     *
     * @param authenticate for getting a principal.
     * @return new pair access + refresh token.
     * @since 4.0
     */
    public JwtTokenDto createToken(Authentication authenticate) {
        JwtUserDetails user = (JwtUserDetails) authenticate.getPrincipal();

        TokenDto accessToken = generateToken(user, ApplicationConstants.ACCESS_TOKEN_SECRET, new Date(new Date().getTime() + ApplicationConstants.ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS));
        TokenDto refreshToken = generateToken(user, ApplicationConstants.REFRESH_TOKEN_SECRET, new Date(new Date().getTime() + ApplicationConstants.REFRESH_TOKEN_EXPIRED_TIME_IN_MILLISECONDS));

        return new JwtTokenDto(accessToken.getToken(), refreshToken.getToken(), accessToken.getExpiredTime());
    }

    /**
     * This method pull out an access token from given HttpServletRequest.
     *
     * @return access token.
     * @since 4.0
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ApplicationConstants.AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(ApplicationConstants.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * This method create an Authentication instance with the information from the passed token.
     *
     * @param token token for parsing.
     * @return Authentication instance with userDetails and authorities.
     * @since 4.0
     */
    public Authentication getAuthenticationWithToken(String token) {

        long userId = tokenParser.getUserIdFromToken(token, ApplicationConstants.ACCESS_TOKEN_SECRET);
        String userName = tokenParser.getUserNameFromToken(token, ApplicationConstants.ACCESS_TOKEN_SECRET);
        Set<GrantedAuthority> authorities = tokenParser.getGrantedAuthoritiesFromToken(token, ApplicationConstants.ACCESS_TOKEN_SECRET);

        JwtUserDetails jwtUserDetails = new JwtUserDetails(userId, userName, null, authorities);

        return new UsernamePasswordAuthenticationToken(jwtUserDetails, "", jwtUserDetails.getAuthorities());
    }


    private TokenDto generateToken(JwtUserDetails user, String currentSecret, Date expiredTime) {

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        String roles = authorities.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));

        Long userId = user.getId();
        String userName = user.getLogin();

        Claims claims = Jwts.claims()
                .setSubject(userName)
                .setId(userId.toString())
                .setAudience(ApplicationConstants.APPLICATION_NAME)
                .setIssuer(ApplicationConstants.APPLICATION_NAME)
                .setIssuedAt(new Date())
                .setExpiration(expiredTime);

        claims.put("roles", roles);

        String tokenStr = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, currentSecret)
                .compact();

        return new TokenDto(tokenStr, expiredTime);
    }
}
