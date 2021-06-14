package com.epam.esm.security.accessdeniedhandler;

import com.epam.esm.security.exceptions.GiftApplicationAccessDeniedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GiftApplicationAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private AccessDeniedException accessDeniedExceptionMock;

    private final AccessDeniedHandler testHandler = new GiftApplicationAccessDeniedHandler();

    @Test
    public void testHandle_ThrowException() {
        //given
        //when
        //then
        Assertions.assertThrows(GiftApplicationAccessDeniedException.class,
                () -> testHandler.handle(requestMock, responseMock, accessDeniedExceptionMock));
    }
}
