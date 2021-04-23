package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.SaveOrderGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * This DTO is validated to fully update a Tag entity.
 *
 * @since 1.0
 */
@Relation(itemRelation = "user", collectionRelation = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonIgnoreProperties(value = {"password"})
public class UserDto extends RepresentationModel<TagDto> {

    @Null(message = "User's id value must be null", groups = {UpdateGroup.class, PatchGroup.class})
    private Long id;

    @NotNull(message = "User's name value must not be null, start with a letter, end with a letter or digit and contain 5-80 symbols")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{5,80}$",
            message = "User's name value must not be null, start with a letter, end with a letter or digit and contain 5-80 symbols")
    private String name;

    @NotNull(message = "User's password value must not be null and contain 5-30 symbols")
    @Pattern(regexp = "^[A-Za-z\\d.-]{5,30}$",
            message = "User's password value must not be null and contain 5-30 symbols")
    private String password;

    @Pattern(regexp = "(?i)(USER)|(ADMIN)", message = "User's role must be only ADMIN or USER and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    @NotBlank(message = "User's role must be only ADMIN or USER and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    private String role;

    @Valid
    private List<OrderDto> orders;

}
