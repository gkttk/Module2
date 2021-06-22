package com.epam.esm.assemblers;

import com.epam.esm.domain.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;

public class UserModelAssemblerTest extends AbstractModelAssemblerTest<UserDto> {

    @Autowired
    private UserModelAssembler modelAssembler;

    @Override
    protected UserModelAssembler getModelAssembler() {
        return modelAssembler;
    }

    @Override
    protected UserDto getDto() {
        return new UserDto().toBuilder()
                .id(1L)
                .login("login")
                .password("pass")
                .role("ADMIN")
                .build();
    }

    //jenkins
}
