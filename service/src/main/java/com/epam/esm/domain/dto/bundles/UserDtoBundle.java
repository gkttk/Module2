package com.epam.esm.domain.dto.bundles;

import com.epam.esm.domain.dto.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDtoBundle {
    private final List<UserDto> users;
    private final long count;
}
