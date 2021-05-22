package com.epam.esm.security.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface JwtObjectMapper<T> {

    UserDetails toJwtUserDetails(T object);

    Set<? extends GrantedAuthority> getGrantedAuthorities(T object);
}
