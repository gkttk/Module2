package com.epam.esm.security;

import com.epam.esm.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public final class JwtUserMapper {


    public static JwtUserDetails toJwtUserDetails(UserDto user) {
        return new JwtUserDetails(
                user.getLogin(),
                user.getPassword(),
                getGrantedAuthorities(user)
        );
    }

    public static Set<GrantedAuthority> getGrantedAuthorities(UserDto user) {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }


}
