package com.epam.esm.querybuilder.parameterparser.parserresult;

import com.epam.esm.querybuilder.parameterparser.enums.Operators;

/**
 * This class is a result of working {@link com.epam.esm.querybuilder.parameterparser.ParameterParser}.
 */
public class ParserResult {

    private final Operators operator;
    private final String[] parameters;

    public ParserResult(Operators operator, String[] parameters) {
        this.operator = operator;
        this.parameters = parameters;
    }

    public Operators getOperator() {
        return operator;
    }


    public String[] getParameters() {
        return parameters;
    }

}
