package com.epam.esm.security.service;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.service.UserService;
import com.epam.esm.security.mapper.JwtObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of {@link org.springframework.security.core.userdetails.UserDetails}.
 * This class looks for a user in DB by it's login and convert it to UserDetails.
 *
 * @since 4.0
 */
@Service(value = "jwtUserDetailsService")
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final JwtObjectMapper<UserDto> objectMapper;

    @Autowired
    public JwtUserDetailsService(UserService userService, JwtObjectMapper<UserDto> objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDto foundUser = userService.findByLogin(login);
        return objectMapper.toJwtUserDetails(foundUser);
    }
}
