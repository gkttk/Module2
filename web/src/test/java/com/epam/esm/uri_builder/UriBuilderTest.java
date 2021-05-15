package com.epam.esm.uri_builder;

import com.epam.esm.config.WebTestConfig;
import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = WebTestConfig.class)
@ExtendWith(SpringExtension.class)
public class UriBuilderTest {

    @Autowired
    private DefaultUriBuilder uriBuilder;

    @Test
    public void testBuildRequestParams_returnCorrectUriResultWithParams_WhenMapWithParamsWasPassed() {
        //given
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("hello", new String[]{"world", "space"});
        parameters.put("bye", new String[]{"2"});
        //when
        UriBuilderResult result = uriBuilder.buildRequestParams(parameters);
        //then
        assertAll(
                () -> assertEquals(result.getLimit(), WebLayerConstants.DEFAULT_LIMIT),
                () -> assertEquals(result.getOffset(), WebLayerConstants.DEFAULT_OFFSET),
                () -> assertTrue(result.getParamString().contains("hello=world")),
                () -> assertTrue(result.getParamString().contains("hello=space")),
                () -> assertTrue(result.getParamString().contains("bye=2"))
        );
    }

    @Test
    public void testBuildRequestParams_returnUriResultWithoutParams_WhenEmptyMapWasPassed() {
        //given
        Map<String, String[]> parameters = Collections.emptyMap();
        //when
        UriBuilderResult result = uriBuilder.buildRequestParams(parameters);
        //then
        assertAll(
                () -> assertEquals(result.getLimit(),  WebLayerConstants.DEFAULT_LIMIT),
                () -> assertEquals(result.getOffset(), WebLayerConstants.DEFAULT_OFFSET),
                () -> assertTrue(result.getParamString().isEmpty())
        );
    }


}
