package com.epam.esm.security.authentrypoint;

import com.epam.esm.security.exceptions.GiftApplicationAuthorizationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UnauthorizedEntryPointTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private AuthenticationException authExceptionMock;

    private final AuthenticationEntryPoint testEntryPoint = new UnauthorizedEntryPoint();

    @Test
    public void testCommence_ThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(GiftApplicationAuthorizationException.class,
                () -> testEntryPoint.commence(requestMock, responseMock, authExceptionMock));
    }
}
