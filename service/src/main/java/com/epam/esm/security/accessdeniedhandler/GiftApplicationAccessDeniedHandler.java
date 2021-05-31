package com.epam.esm.security.accessdeniedhandler;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.security.exceptions.GiftApplicationAccessDeniedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This handler throws a custom application exception when there is no access to resource.
 *
 * @since 4.0
 */
public class GiftApplicationAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw new GiftApplicationAccessDeniedException("Access denied", ApplicationConstants.ACCESS_DENIED_ERROR_CODE);
    }
}
