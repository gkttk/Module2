package com.epam.esm.security.service;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.security.mapper.JwtObjectMapper;
import com.epam.esm.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
