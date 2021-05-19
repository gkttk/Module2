package com.epam.esm.config;

import com.epam.esm.security.JwtConfigurer;
import com.epam.esm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()

                .antMatchers(HttpMethod.GET, "/certificates/**").permitAll()

                .regexMatchers(HttpMethod.POST, "/users/(\\d+)/orders").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/**").hasAnyAuthority("USER", "ADMIN")

                .antMatchers(HttpMethod.POST,"/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH,"/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(tokenProvider));

    }
}
