package com.epam.esm.controller;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.domain.dto.token.JwtTokenDto;
import com.epam.esm.domain.dto.token.LoginPasswordDto;
import com.epam.esm.security.JwtTokenProvider;
import com.epam.esm.security.blacklist.JwtTokenBlackList;
import com.epam.esm.security.exceptions.GiftApplicationAuthorizationException;
import com.epam.esm.security.exceptions.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping(path = "/auth", produces = {"application/json; charset=UTF-8"})
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final JwtTokenBlackList jwtTokenBlackList;

    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, JwtTokenBlackList jwtTokenBlackList) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.jwtTokenBlackList = jwtTokenBlackList;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDto> authenticate(@RequestBody LoginPasswordDto loginPasswordDto) {
        String login = loginPasswordDto.getLogin();
        String password = loginPasswordDto.getPassword();
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
            JwtTokenDto token = tokenProvider.createToken(authenticate);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException ex) {
            throw new GiftApplicationAuthorizationException("Invalid credentials", WebLayerConstants.INVALID_CREDENTIALS_ERROR_CODE,
                    login, password);
        }

    }

    @PostMapping("/refresh_token")
    public ResponseEntity<JwtTokenDto> refreshToken(WebRequest request) {
        String tokenFromRequest = getTokenFromRequest(request);
        JwtTokenDto newToken = tokenProvider.refreshToken(tokenFromRequest);
        return ResponseEntity.ok(newToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(WebRequest request) {
        String tokenFromRequest = getTokenFromRequest(request);
        jwtTokenBlackList.add(tokenFromRequest);
        return ResponseEntity.noContent().build();
    }


    private String getTokenFromRequest(WebRequest request) {
        String authHeader = request.getHeader(WebLayerConstants.AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(WebLayerConstants.BEARER_PREFIX)) {
            return authHeader.substring(7);
        }
        throw new JwtAuthenticationException("Missing access token in request.", WebLayerConstants.ACCESS_TOKEN_NOT_FOUND);
    }

}
