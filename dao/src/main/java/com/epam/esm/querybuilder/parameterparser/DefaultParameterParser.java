package com.epam.esm.querybuilder.parameterparser;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.querybuilder.parameterparser.enums.Operators;
import com.epam.esm.querybuilder.parameterparser.parserresult.ParserResult;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Default implementation of {@link com.epam.esm.querybuilder.parameterparser.ParameterParser} interface.
 */
@Component
public class DefaultParameterParser implements ParameterParser {

    public ParserResult parseRequestParameter(String requestParameter) {
        int columnIndex = requestParameter.indexOf(":");
        String valuesString = requestParameter.substring(columnIndex + 1);
        String[] values = valuesString.split(ApplicationConstants.REGEX_FOR_SPLIT_PARAMETERS);
        String[] trimmedValues = Stream.of(values).map(String::trim).toArray(String[]::new);
        if (columnIndex != -1) {
            String operatorName = requestParameter.substring(0, columnIndex);
            Operators operator = Operators.findOperatorByName(operatorName);
            return new ParserResult(operator, trimmedValues);
        } else {
            return new ParserResult(Operators.NONE, trimmedValues);
        }


    }


}
