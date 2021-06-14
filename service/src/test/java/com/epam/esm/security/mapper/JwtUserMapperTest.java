package com.epam.esm.security.mapper;

import com.epam.esm.domain.dto.UserDto;
import com.epam.esm.security.JwtUserDetails;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtUserMapperTest {

    private JwtObjectMapper<UserDto> jwtObjectMapper = new JwtUserMapper();
    private static UserDto USER_DTO;
    private static UserDetails EXPECTED_USER_DETAILS;

    @BeforeAll
    static void init() {
        long userId = 1L;
        String userLogin = "Login";
        String userPass = "Pass";
        String role = "ADMIN";

        USER_DTO = new UserDto();
        USER_DTO.setId(userId);
        USER_DTO.setLogin(userLogin);
        USER_DTO.setPassword(userPass);
        USER_DTO.setRole(role);

        EXPECTED_USER_DETAILS = new JwtUserDetails(userId, userLogin, userPass,
                Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    @Test
    public void testToJwtUserDetails_ShouldMapUserDtoToJwtUserDetails() {
        //given
        //when
        UserDetails result = jwtObjectMapper.toJwtUserDetails(USER_DTO);
        //then
        assertEquals(result, EXPECTED_USER_DETAILS);
    }
}
