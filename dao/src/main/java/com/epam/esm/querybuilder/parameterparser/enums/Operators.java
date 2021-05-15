package com.epam.esm.querybuilder.parameterparser.enums;

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
     */
    public static Operators findOperatorByName(String name) {
        return Stream.of(Operators.values())
                .filter(operator -> operator.name().equalsIgnoreCase(name))
                .findFirst().orElse(Operators.NONE);
    }

}
