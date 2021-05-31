package com.epam.esm.security.mapper;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.security.JwtUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Default implementation of {@link com.epam.esm.security.mapper.JwtObjectMapper}
 */
@Component
public class JwtUserMapper implements JwtObjectMapper<UserDto> {

    @Override
    public JwtUserDetails toJwtUserDetails(UserDto user) {
        return new JwtUserDetails(user.getId(), user.getLogin(),
                user.getPassword(),
                getGrantedAuthorities(user)
        );
    }

    @Override
    public Set<GrantedAuthority> getGrantedAuthorities(UserDto user) {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
    }


}
