package com.epam.esm.security.token;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.domain.dto.token.JwtTokenDto;
import com.epam.esm.security.JwtUserDetails;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import com.epam.esm.security.token.JwtTokenProvider;
import com.epam.esm.security.token.parser.TokenParser;
import com.epam.esm.security.token.validator.TokenValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenProviderTest {

    @Mock
    private TokenValidator tokenValidator;

    @Mock
    private TokenParser tokenParser;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private HttpServletRequest requestMock;

    @InjectMocks
    private JwtTokenProvider tokenProvider;

    private static String ACCESS_TOKEN;
    private static String REFRESH_TOKEN;
    private static long USER_ID;
    private static String USER_LOGIN;
    private static Set<GrantedAuthority> AUTHORITIES;
    private static JwtUserDetails USER_DETAILS_TEST;

    @BeforeAll
    static void init() {
        ACCESS_TOKEN = "accessToken";
        REFRESH_TOKEN = "refreshToken";
        USER_ID = 1L;
        USER_LOGIN = "name";
        AUTHORITIES = Collections.emptySet();
        USER_DETAILS_TEST = new JwtUserDetails(USER_ID, USER_LOGIN, null, AUTHORITIES);
    }

    @Test
    public void testRefreshToken_ReturnNewJwtTokenPair_WhenGivenRefreshTokenIsValid() {
        //given
        when(tokenValidator.validateRefreshToken(REFRESH_TOKEN)).thenReturn(true);
        when(tokenParser.getUserIdFromToken(eq(REFRESH_TOKEN), anyString())).thenReturn(USER_ID);
        when(tokenParser.getUserNameFromToken(eq(REFRESH_TOKEN), anyString())).thenReturn(USER_LOGIN);
        when(tokenParser.getGrantedAuthoritiesFromToken(eq(REFRESH_TOKEN), anyString())).thenReturn(AUTHORITIES);
        when(tokenParser.getExpirationFromToken(eq(REFRESH_TOKEN), anyString())).thenReturn(Date.from(Instant.EPOCH));
        //when
        JwtTokenDto result = tokenProvider.refreshToken(REFRESH_TOKEN);
        //then
        verify(tokenValidator).validateRefreshToken(REFRESH_TOKEN);
        verify(tokenParser).getUserIdFromToken(eq(REFRESH_TOKEN), anyString());
        verify(tokenParser).getUserNameFromToken(eq(REFRESH_TOKEN), anyString());
        verify(tokenParser).getGrantedAuthoritiesFromToken(eq(REFRESH_TOKEN), anyString());
        verify(tokenParser).getExpirationFromToken(eq(REFRESH_TOKEN), anyString());
        assertNotNull(result);
    }

    @Test
    public void testRefreshToken_ThrowException_WhenGivenRefreshTokenIsInvalid() {
        //given
        when(tokenValidator.validateRefreshToken(REFRESH_TOKEN)).thenReturn(false);
        //when
        //then
        assertThrows(JwtAuthenticationException.class, () -> tokenProvider.refreshToken(REFRESH_TOKEN));
        verify(tokenValidator).validateRefreshToken(REFRESH_TOKEN);
    }

    @Test
    public void testCreateToken_ShouldCreateJwtTokenDto() {
        //given
        when(authenticationMock.getPrincipal()).thenReturn(USER_DETAILS_TEST);
        //when
        JwtTokenDto result = tokenProvider.createToken(authenticationMock);
        //then
        assertAll(
                () -> assertNotNull(result.getAccessToken()),
                () -> assertNotNull(result.getRefreshToken()),
                () -> assertNotNull(result.getExpirationTime())
        );
    }

    @Test
    public void testResolveToken_ShouldReturnTokenFromRequest_WhenHeaderStartsWithBearerPrefix() {
        //given
        when(requestMock.getHeader(ApplicationConstants.AUTH_HEADER)).thenReturn("Bearer " + ACCESS_TOKEN);
        //when
        String result = tokenProvider.resolveToken(requestMock);
        //then
        assertNotNull(result);
    }

    @Test
    public void testResolveToken_ReturnNull_WhenHeaderDoesNotStartWithBearerPrefix() {
        //given
        when(requestMock.getHeader(ApplicationConstants.AUTH_HEADER)).thenReturn("incorrectHeader");
        //when
        String result = tokenProvider.resolveToken(requestMock);
        //then
        assertNull(result);
    }

    @Test
    public void testResolveToken_ReturnNull_WhenHeaderIsNull() {
        //given
        when(requestMock.getHeader(ApplicationConstants.AUTH_HEADER)).thenReturn(null);
        //when
        String result = tokenProvider.resolveToken(requestMock);
        //then
        assertNull(result);
    }

    @Test
    public void testGetAuthenticationWithToken_ReturnFilledAuthentication() {
        //given
        when(tokenParser.getUserIdFromToken(eq(ACCESS_TOKEN), anyString())).thenReturn(USER_ID);
        when(tokenParser.getUserNameFromToken(eq(ACCESS_TOKEN), anyString())).thenReturn(USER_LOGIN);
        when(tokenParser.getGrantedAuthoritiesFromToken(eq(ACCESS_TOKEN), anyString())).thenReturn(AUTHORITIES);

        Authentication expectedResult = new UsernamePasswordAuthenticationToken(USER_DETAILS_TEST,
                "", USER_DETAILS_TEST.getAuthorities());
        //when
        Authentication result = tokenProvider.getAuthenticationWithToken(ACCESS_TOKEN);
        //then
        assertEquals(result, expectedResult);
    }
}
