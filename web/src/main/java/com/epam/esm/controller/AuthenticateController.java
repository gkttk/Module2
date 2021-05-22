package com.epam.esm.controller;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.domain.dto.login.JwtTokenDto;
import com.epam.esm.domain.dto.login.LoginPasswordDto;
import com.epam.esm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth", produces = {"application/json; charset=UTF-8"})
public class AuthenticateController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticateController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDto> authenticate(@RequestBody LoginPasswordDto loginPasswordDto) {
        String login = loginPasswordDto.getLogin();
        String password = loginPasswordDto.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        JwtTokenDto token = tokenProvider.createToken(login);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<JwtTokenDto> refreshToken(@RequestHeader(value = WebLayerConstants.AUTH_HEADER) String authHeader) {

        if (authHeader != null && authHeader.startsWith(WebLayerConstants.BEARER_PREFIX)) {
            String oldAccessToken = authHeader.substring(7);
            JwtTokenDto newToken = tokenProvider.refreshToken(oldAccessToken);
            return ResponseEntity.ok(newToken);

        }
        throw new IllegalStateException("");//todo
    }

}
