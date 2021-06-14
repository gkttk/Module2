package com.epam.esm.security.token.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link com.epam.esm.security.token.parser.TokenParser}
 *
 * @since 5.0
 */
@Component
public class JwtTokenParser implements TokenParser {

    @Override
    public Date getExpirationFromToken(String token, String currentSecret) {
        return Jwts.parser().setSigningKey(currentSecret).parseClaimsJws(token).getBody().getExpiration();
    }

    @Override
    public String getUserNameFromToken(String token, String currentSecret) {
        return Jwts.parser().setSigningKey(currentSecret).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public long getUserIdFromToken(String token, String currentSecret) {
        return Long.parseLong(
                Jwts.parser().
                        setSigningKey(currentSecret)
                        .parseClaimsJws(token)
                        .getBody()
                        .getId()
        );
    }

    @Override
    public Set<GrantedAuthority> getGrantedAuthoritiesFromToken(String token, String currentSecret) {
        Claims payload = Jwts.parser()
                .setSigningKey(currentSecret)
                .parseClaimsJws(token).getBody();

        String[] roles = payload.get("roles", String.class).split(",");

        return Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
