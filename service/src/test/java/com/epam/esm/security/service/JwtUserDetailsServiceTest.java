package com.epam.esm.security.service;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.domain.service.UserService;
import com.epam.esm.security.JwtUserDetails;
import com.epam.esm.security.mapper.JwtObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtObjectMapper<UserDto> objectMapper;

    @InjectMocks
    private JwtUserDetailsService userDetailsService;

    private static UserDto USER_DTO;
    private static UserDetails USER_DETAILS;
    private static String USER_NAME = "Login";

    @BeforeAll
    static void init() {
        long userId = 1L;
        String userPass = "Pass";
        String role = "ADMIN";

        USER_DTO = new UserDto();
        USER_DTO.setId(userId);
        USER_DTO.setLogin(USER_NAME);
        USER_DTO.setPassword(userPass);
        USER_DTO.setRole(role);
        USER_DETAILS = new JwtUserDetails(userId, USER_NAME, userPass,
                Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    @Test
    public void testLoadUserByUsername_ReturnFilledUserDetails() {
        //given
        when(userService.findByLogin(USER_NAME)).thenReturn(USER_DTO);
        when(objectMapper.toJwtUserDetails(USER_DTO)).thenReturn(USER_DETAILS);
        //when
        UserDetails result = userDetailsService.loadUserByUsername(USER_NAME);
        //then
        Assertions.assertEquals(result, USER_DETAILS);
    }
}
