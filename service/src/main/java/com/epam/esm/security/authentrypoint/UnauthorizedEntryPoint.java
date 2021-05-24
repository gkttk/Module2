package com.epam.esm.security.authentrypoint;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.security.exceptions.GiftApplicationUnauthorizedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        throw new GiftApplicationUnauthorizedException("User is not authorized.", ApplicationConstants.UNAUTHORIZED_ERROR_CODE);
    }
}
