package com.epam.esm.domain.dto;

import com.epam.esm.domain.dto.groups.SaveGroup;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * User DTO.
 *
 * @since 2.0
 */
@Relation(itemRelation = "user", collectionRelation = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonFilter("passwordFilter")
public class UserDto extends RepresentationModel<UserDto> {

    @Null(message = "{user_dto_id_violation_message}", groups = SaveGroup.class)
    private Long id;

    @NotNull(message = "{user_dto_login_violation_message}", groups = SaveGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{3,80}$",
            message = "{user_dto_login_violation_message}", groups = SaveGroup.class)
    private String login;

    @NotNull(message = "{user_dto_password_violation_message}", groups = SaveGroup.class)
    @Pattern(regexp = "^[A-Za-z\\d.-]{5,30}$", message = "{user_dto_password_violation_message}", groups = SaveGroup.class)
    private String password;

    @Null(message = "{user_dto_role_violation_message}", groups = SaveGroup.class)
    private String role;
}
