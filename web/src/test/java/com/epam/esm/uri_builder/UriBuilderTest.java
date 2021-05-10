package com.epam.esm.uri_builder;

import com.epam.esm.config.WebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = WebTestConfig.class)
@ExtendWith(SpringExtension.class)
public class UriBuilderTest {

    @Autowired
    private DefaultUriBuilder uriBuilder;

    @Test
    public void testBuildRequestParams_returnCorrectStringWithParams_WhenMapWithParamsWasPassed() {
        //given
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("hello", new String[]{"world", "space"});
        parameters.put("bye", new String[]{"2"});
        //when
        String result = uriBuilder.buildRequestParams(parameters);
        //then
        assertAll(
                () -> assertTrue(result.contains("hello=world")),
                () -> assertTrue(result.contains("hello=space")),
                () -> assertTrue(result.contains("bye=2"))
        );
    }

    @Test
    public void testBuildRequestParams_returnEmptyString_WhenEmptyMapWasPassed() {
        //given
        Map<String, String[]> parameters = Collections.emptyMap();
        //when
        String result = uriBuilder.buildRequestParams(parameters);
        //then
        assertTrue(result.isEmpty());
    }


}
