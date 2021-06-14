package com.epam.esm.security.filters;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.security.JwtUserDetails;
import com.epam.esm.security.exceptions.GiftApplicationAccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This filter is responsible for preventing making an order on URI {@link com.epam.esm.constants.ApplicationConstants#MAKE_ORDER_URI}
 * for users with mismatched ID. The filter compares if of an authenticated user and user id passed in URI.
 *
 * @since 4.0
 */
public class UserIdFilter extends OncePerRequestFilter {

    private final RequestMatcher customFilterUrl;
    private final Pattern pattern;

    public UserIdFilter() {
        this.customFilterUrl = new AntPathRequestMatcher(ApplicationConstants.MAKE_ORDER_URI);
        this.pattern = Pattern.compile(ApplicationConstants.USER_ID_MAKE_ORDER_REGEX);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (customFilterUrl.matches(request)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                JwtUserDetails user = (JwtUserDetails) authentication.getPrincipal();
                long authUserId = user.getId();
                Matcher matcher = pattern.matcher(request.getRequestURI());
                if (matcher.find()) {
                    long idFromUrl = Long.parseLong(matcher.group());
                    if (idFromUrl != authUserId) {
                        throw new GiftApplicationAccessDeniedException("Access denied", ApplicationConstants.ACCESS_DENIED_ERROR_CODE);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
