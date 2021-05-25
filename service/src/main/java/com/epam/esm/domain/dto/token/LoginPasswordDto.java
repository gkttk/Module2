package com.epam.esm.domain.dto.token;

import lombok.Data;

@Data
public class LoginPasswordDto {
    private String login;
    private String password;
}
