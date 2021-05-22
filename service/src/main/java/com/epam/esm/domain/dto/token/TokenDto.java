package com.epam.esm.domain.dto.token;

import lombok.Data;

import java.util.Date;

@Data
public class TokenDto {
    private final String token;
    private final Date expiredTime;
}
