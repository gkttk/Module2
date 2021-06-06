package com.epam.esm.domain.dto.token;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginPasswordDto {
    @NotNull(message = "{login_password_dto_login_violation_message}")
    private String login;
    @NotNull(message = "{login_password_dto_password_violation_message}")
    private String password;
}
