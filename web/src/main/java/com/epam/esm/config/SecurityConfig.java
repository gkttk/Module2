package com.epam.esm.config;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.security.accessdeniedhandler.GiftApplicationAccessDeniedHandler;
import com.epam.esm.security.authentrypoint.UnauthorizedEntryPoint;
import com.epam.esm.security.filters.JwtTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider tokenProvider;
    private final HandlerExceptionResolver resolver;

    @Autowired
    public SecurityConfig(JwtTokenProvider tokenProvider,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.tokenProvider = tokenProvider;
        this.resolver = resolver;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(WebLayerConstants.ALL_AUTH_URL_REGEX_PATTERN).and();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(WebLayerConstants.ALL_AUTH_URL_REGEX_PATTERN).permitAll()
                .antMatchers(HttpMethod.GET, "/certificates/**").permitAll()
                .regexMatchers(HttpMethod.POST, "/users/(\\d+)/orders").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, WebLayerConstants.ALL_URL_REGEX_PATTERN).hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, WebLayerConstants.ALL_URL_REGEX_PATTERN).hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, WebLayerConstants.ALL_URL_REGEX_PATTERN).hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, WebLayerConstants.ALL_URL_REGEX_PATTERN).hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, WebLayerConstants.ALL_URL_REGEX_PATTERN).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authenticationEntryPoint())

                .and()
                .addFilterBefore(new JwtTokenAuthenticationFilter(tokenProvider, resolver), BasicAuthenticationFilter.class);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new GiftApplicationAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedEntryPoint();
    }


}
