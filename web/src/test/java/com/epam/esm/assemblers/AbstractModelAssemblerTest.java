package com.epam.esm.assemblers;

import com.epam.esm.config.WebTestConfig;
import com.epam.esm.constants.WebLayerConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes = WebTestConfig.class)
@ExtendWith(SpringExtension.class)
public abstract class AbstractModelAssemblerTest<T extends RepresentationModel<T>> {

    private static final long TEST_COUNT = 1000;

    protected abstract T getDto();

    protected abstract ModelAssembler<T> getModelAssembler();

    @Test
    public void testToModel_ReturnDtoWithLinks() {
        //given
        ModelAssembler<T> modelAssembler = getModelAssembler();
        //when
        T result = modelAssembler.toModel(getDto());
        //then
        assertFalse(result.getLinks().isEmpty());
    }

    @Test
    public void testToCollectionModel_ReturnCollectionOfDtoWithLinks() {
        //given
        ModelAssembler<T> modelAssembler = getModelAssembler();
        //when
        CollectionModel<T> result = modelAssembler.toCollectionModel(Arrays.asList(getDto(), getDto()), WebLayerConstants.DEFAULT_OFFSET, TEST_COUNT, Collections.emptyMap(), "1");
        //then
        assertFalse(result.getLinks().isEmpty());
    }

}
