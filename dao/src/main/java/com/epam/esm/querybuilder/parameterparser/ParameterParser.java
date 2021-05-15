package com.epam.esm.querybuilder.parameterparser;

import com.epam.esm.querybuilder.parameterparser.parserresult.ParserResult;

/**
 * This interface represents an api for parsing request parameter to parameters and logical operator.
 */
public interface ParameterParser {

    /**
     * This method parses a string to operator and an array of parameters.
     *
     * @param requestParameter parameter of request.
     * @return {@link com.epam.esm.querybuilder.parameterparser.parserresult.ParserResult} instance with parsed request parameter.
     */
    ParserResult parseRequestParameter(String requestParameter);
}
