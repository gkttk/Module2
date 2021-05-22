package com.epam.esm.domain.dto.login;

import lombok.Data;

import java.util.Date;

@Data
public class JwtTokenDto {
    private final String accessToken;
    private final String refreshToken;
    private final Date expirationTime;

}
