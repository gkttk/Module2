package com.epam.esm.assemblers;


import com.epam.esm.domain.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;


public class TagModelAssemblerTest extends AbstractModelAssemblerTest<TagDto> {

    @Autowired
    private TagModelAssembler modelAssembler;

    @Override
    protected TagModelAssembler getModelAssembler() {
        return modelAssembler;
    }

    @Override
    protected TagDto getDto() {
        return new TagDto(1L, "tag");
    }


}
