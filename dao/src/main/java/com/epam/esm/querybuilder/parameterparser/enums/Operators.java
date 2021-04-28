package com.epam.esm.querybuilder.parameterparser.enums;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.exceptions.RequestParameterParserException;

import java.util.stream.Stream;

/**
 * This enum represents an operators which is passed in request parameter for searching and construct query.
 */
public enum Operators {
    AND, OR, NONE;

    /**
     * This method makes an attempt to compare the passed name with names of Operator enum values.
     *
     * @param name name for matching.
     * @return Operator with the same name as passed.
     * @throws RequestParameterParserException if there are no matches here.
     */
    public static Operators findOperatorByName(String name) {
        return Stream.of(Operators.values())
                .filter(operator -> operator.name().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new RequestParameterParserException(ApplicationConstants.INCORRECT_OPERATOR_VALUE,
                        String.format("Incorrect name of Operator in request parameter: %s", name)));
    }

}
