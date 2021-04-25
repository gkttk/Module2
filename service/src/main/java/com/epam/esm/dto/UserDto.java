package com.epam.esm.dto;

import com.epam.esm.dto.groups.SaveGroup;
import com.fasterxml.jackson.annotation.JsonFilter;
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
 * This DTO is validated to fully update a Tag entity.
 *
 * @since 1.0
 */
@Relation(itemRelation = "user", collectionRelation = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonFilter("passwordFilter")
public class UserDto extends RepresentationModel<TagDto> {

    @Null(message = "User's id value must be null", groups = SaveGroup.class)
    private Long id;

    @NotNull(message = "User's login value must not be null, start with a letter, end with a letter or digit and contain 6-80 symbols", groups = SaveGroup.class)
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{5,80}$",
            message = "User's login value must not be null, start with a letter, end with a letter or digit and contain 6-80 symbols", groups = SaveGroup.class)
    private String login;

    @NotNull(message = "User's password value must not be null and contain 5-30 symbols", groups = SaveGroup.class)
    @Pattern(regexp = "^[A-Za-z\\d.-]{5,30}$", message = "User's password value must not be null and contain 5-30 symbols", groups = SaveGroup.class)
    private String password;

    @Pattern(regexp = "(?i)(USER)|(ADMIN)", message = "User's role must be only ADMIN or USER and not be null", groups = SaveGroup.class)
    @NotBlank(message = "User's role must be only ADMIN or USER and not be null", groups = SaveGroup.class)
    private String role;
}
