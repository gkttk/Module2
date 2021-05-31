package com.epam.esm.security;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.security.TokenPairDao;
import com.epam.esm.domain.dto.token.JwtTokenDto;
import com.epam.esm.domain.dto.token.TokenDto;
import com.epam.esm.entity.TokenPair;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * This class is responsible for access and refresh tokens managing.
 *
 * @since 4.0
 */
@Component
public class JwtTokenProvider {

    private final TokenPairDao refreshTokenDao;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(TokenPairDao refreshTokenDao,
                            @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.refreshTokenDao = refreshTokenDao;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method checks validity of refresh token in DB(if it exists there) and generates a new token pair(access+refresh)
     * for returning to client.
     *
     * @param oldAccessToken expired access token for searching the relevant refresh token in DB.
     * @return new pair access + refresh token.
     * @since 4.0
     */
    @Transactional
    public JwtTokenDto refreshToken(String oldAccessToken) {
        TokenPair foundToken = refreshTokenDao.findByAccessToken(oldAccessToken);
        if (foundToken != null) {
            Date expiredTime = foundToken.getRefreshTokenExpiredTime();
            if (!expiredTime.before(new Date())) {
                String userName = getUserName(oldAccessToken, ApplicationConstants.ACCESS_TOKEN_SECRET);
                TokenDto accessToken = generateToken(userName, ApplicationConstants.ACCESS_TOKEN_SECRET, ApplicationConstants.ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);
                TokenDto refreshToken = generateToken(userName, ApplicationConstants.REFRESH_TOKEN_SECRET, ApplicationConstants.REFRESH_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);

                foundToken.setAccessToken(accessToken.getToken());
                foundToken.setRefreshToken(refreshToken.getToken());
                refreshTokenDao.save(foundToken);
                return new JwtTokenDto(accessToken.getToken(), refreshToken.getToken(), accessToken.getExpiredTime());
            } else {
                refreshTokenDao.delete(foundToken);
            }
        }
        throw new JwtAuthenticationException("Refresh token is expired", ApplicationConstants.REFRESH_TOKEN_EXPIRED);
    }

    /**
     * This method create a token pair(access+refresh).
     *
     * @param authenticate for getting a principal.
     * @return new pair access + refresh token.
     * @since 4.0
     */
    @Transactional
    public JwtTokenDto createToken(Authentication authenticate) {
        JwtUserDetails user = (JwtUserDetails) authenticate.getPrincipal();

        String userName = user.getLogin();
        TokenDto accessToken = generateToken(userName, ApplicationConstants.ACCESS_TOKEN_SECRET, ApplicationConstants.ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);
        TokenDto refreshToken = generateToken(userName, ApplicationConstants.REFRESH_TOKEN_SECRET, ApplicationConstants.REFRESH_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);

        refreshTokenDao.save(new TokenPair(accessToken.getToken(), refreshToken.getToken(), refreshToken.getExpiredTime()));
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
     * This method validates a given token.
     *
     * @return is the given token valid.
     * @since 4.0
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(ApplicationConstants.ACCESS_TOKEN_SECRET)
                    .parseClaimsJws(token);
            boolean isTokenExist = refreshTokenDao.existsByAccessToken(token);
            boolean isTokenExpired = claims.getBody().getExpiration().before(new Date());
            return !isTokenExpired && isTokenExist;
        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("JWT access token is expired", ApplicationConstants.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is invalid ", ApplicationConstants.ACCESS_TOKEN_INVALID);
        }
    }

    /**
     * This method removes token pair(access+refresh) from DB by given access token.
     *
     * @param token access token
     * @since 4.0
     */
    @Transactional
    public void removeToken(String token) {
        refreshTokenDao.deleteAllByAccessToken(token);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token, ApplicationConstants.ACCESS_TOKEN_SECRET));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    private String getUserName(String token, String currentSecret) {
        return Jwts.parser().setSigningKey(currentSecret).parseClaimsJws(token).getBody().getSubject();
    }


    private TokenDto generateToken(String userName, String currentSecret, long expiredTime) {
        Claims claims = Jwts.claims().setSubject(userName);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredTime);
        String tokenStr = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, currentSecret)
                .compact();

        return new TokenDto(tokenStr, validity);
    }


}
