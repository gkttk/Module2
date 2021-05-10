package com.epam.esm.uri_builder;

import java.util.Map;

public interface UriBuilder {

    String buildRequestParams(Map<String, String[]> parameterMap);
}
