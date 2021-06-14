package com.epam.esm.uri_builder;

import com.epam.esm.constants.WebLayerConstants;
import com.epam.esm.uri_builder.result.UriBuilderResult;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class DefaultUriBuilder implements UriBuilder{

    public UriBuilderResult buildRequestParams(Map<String, String[]> parameterMap) {

        UriBuilderResult result = new UriBuilderResult();

        if (parameterMap == null || parameterMap.isEmpty()){
            return result;
        }

        StringJoiner stringJoiner = new StringJoiner("&", "?", "");
        parameterMap.entrySet()
                .stream()
                .distinct()
                .forEach(entry -> {
                    String key = entry.getKey();
                    if (WebLayerConstants.LIMIT.equalsIgnoreCase(key)){
                        result.setLimit(Integer.parseInt(entry.getValue()[0]));
                        return;
                    }
                    if (WebLayerConstants.OFFSET.equalsIgnoreCase(key)){
                        result.setOffset(Integer.parseInt(entry.getValue()[0]));
                        return;
                    }
                    Arrays.stream(entry.getValue())
                            .forEach(value -> {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(key)
                                        .append("=")
                                        .append(value);
                                stringJoiner.add(stringBuilder);

                            });
                });
        if (!"?".equalsIgnoreCase(stringJoiner.toString())){
            result.setParamString(stringJoiner.toString());
        }

        return result;
    }
}
