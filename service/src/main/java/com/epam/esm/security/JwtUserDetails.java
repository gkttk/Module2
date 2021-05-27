package com.epam.esm.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


public class JwtUserDetails implements UserDetails {

    private final Long id;
    private final String login;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public JwtUserDetails(Long id, String login, String password, Set<GrantedAuthority> grantedAuthorities) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authorities = grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getLogin() {
        return login;
    }


}
