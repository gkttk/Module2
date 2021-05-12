package com.epam.esm.uri_builder;

import com.epam.esm.uri_builder.result.UriBuilderResult;

import java.util.Map;

public interface UriBuilder {

    UriBuilderResult buildRequestParams(Map<String, String[]> parameterMap);
}
