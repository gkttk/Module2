package com.epam.esm.uri_builder;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class DefaultUriBuilder implements UriBuilder{

    public String buildRequestParams(Map<String, String[]> parameterMap) {

        if (parameterMap == null || parameterMap.isEmpty()){
            return "";
        }

        StringJoiner stringJoiner = new StringJoiner("&", "?", "");
        parameterMap.entrySet()
                .stream()
                .distinct()
                .forEach(entry -> {
                    String key = entry.getKey();
                    Arrays.stream(entry.getValue())
                            .forEach(value -> {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(key)
                                        .append("=")
                                        .append(value);
                                stringJoiner.add(stringBuilder);

                            });
                });

        return stringJoiner.toString();

    }


}
