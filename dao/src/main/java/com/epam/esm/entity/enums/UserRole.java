package com.epam.esm.entity.enums;

import java.util.stream.Stream;

public enum UserRole {
    USER, ADMIN;


    public static UserRole searchRole(String roleStr) {
        return Stream.of(UserRole.values())
                .filter(role -> role.name().equalsIgnoreCase(roleStr))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(UserRole.class, roleStr));//todo
    }


}
