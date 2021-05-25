package com.epam.esm.security;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.security.RefreshTokenDao;
import com.epam.esm.domain.dto.token.JwtTokenDto;
import com.epam.esm.domain.dto.token.TokenDto;
import com.epam.esm.entity.RefreshToken;
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

@Component
public class JwtTokenProvider {

    private final RefreshTokenDao refreshTokenDao;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(RefreshTokenDao refreshTokenDao,
                            @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.refreshTokenDao = refreshTokenDao;
        this.userDetailsService = userDetailsService;
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

    @Transactional
    public JwtTokenDto refreshToken(String oldAccessToken) {
        RefreshToken foundToken = refreshTokenDao.findByAccessToken(oldAccessToken);
        if (foundToken != null) {
            Date expiredTime = foundToken.getExpiredTime();
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

    @Transactional
    public JwtTokenDto createToken(String userName) {
        TokenDto accessToken = generateToken(userName, ApplicationConstants.ACCESS_TOKEN_SECRET, ApplicationConstants.ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);
        TokenDto refreshToken = generateToken(userName, ApplicationConstants.REFRESH_TOKEN_SECRET, ApplicationConstants.REFRESH_TOKEN_EXPIRED_TIME_IN_MILLISECONDS);

        refreshTokenDao.save(new RefreshToken(accessToken.getToken(), refreshToken.getToken(), refreshToken.getExpiredTime()));
        return new JwtTokenDto(accessToken.getToken(), refreshToken.getToken(), accessToken.getExpiredTime());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token, ApplicationConstants.ACCESS_TOKEN_SECRET));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    private String getUserName(String token, String currentSecret) {
        return Jwts.parser().setSigningKey(currentSecret).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(ApplicationConstants.AUTH_HEADER);
        if (bearerToken != null && bearerToken.startsWith(ApplicationConstants.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(ApplicationConstants.ACCESS_TOKEN_SECRET)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            throw new JwtAuthenticationException("JWT access token is expired", ApplicationConstants.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is invalid ", ApplicationConstants.ACCESS_TOKEN_INVALID);
        }

    }


}
