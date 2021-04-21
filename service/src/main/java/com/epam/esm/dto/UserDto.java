package com.epam.esm.dto;

import com.epam.esm.dto.groups.PatchGroup;
import com.epam.esm.dto.groups.SaveOrderGroup;
import com.epam.esm.dto.groups.UpdateGroup;
import com.epam.esm.entity.Order;

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
public class UserDto {

    @Null(message = "User's id value must be null", groups = {UpdateGroup.class, PatchGroup.class})
    @NotNull(groups = {SaveOrderGroup.class}, message = "User's id value must not be null")
    private Long id;

    @Pattern(regexp = "(?i)(USER)|(ADMIN)", message = "User's role must be only ADMIN or USER and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    @NotBlank(message = "User's role must be only ADMIN or USER and not be null", groups = {UpdateGroup.class, PatchGroup.class})
    private String role;
    @Valid
    private List<OrderDto> orders;

    public UserDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDto> orders) {
        this.orders = orders;
    }



}
