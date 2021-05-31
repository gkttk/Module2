package com.epam.esm.security.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * This interface provides an api of converting an object to UserDetails instance.
 *
 * @param <T> object which will be converted to UserDetails
 * @since 4.0
 */
public interface JwtObjectMapper<T> {

    UserDetails toJwtUserDetails(T object);

    Set<? extends GrantedAuthority> getGrantedAuthorities(T object);
}
