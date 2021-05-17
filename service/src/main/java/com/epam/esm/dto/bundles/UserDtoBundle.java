package com.epam.esm.dto.bundles;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDtoBundle {
    private final List<UserDto> users;
    private final long count;
}
