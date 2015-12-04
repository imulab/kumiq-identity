package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public enum LogicalOperator implements OperatorAware {

    AND(ScimConstants.FILTER_AND, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    OR(ScimConstants.FILTER_OR, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    NOT(ScimConstants.FILTER_NOT, DEFAULT_PRECEDENCE, Associtivity.RIGHT);

    private final String operatorString;
    private final int precedence;
    private final Associtivity associtivity;

    LogicalOperator(String operatorString, int precedence, Associtivity associtivity) {
        this.operatorString = operatorString;
        this.precedence = precedence;
        this.associtivity = associtivity;
    }

    public String getOperatorString() {
        return operatorString;
    }

    @Override
    public String toString() {
        return operatorString;
    }

    @Override
    public int precedence() {
        return this.precedence;
    }

    @Override
    public Associtivity associtivity() {
        return this.associtivity;
    }

    public static LogicalOperator fromString(String operatorString){
        if(AND.operatorString.equalsIgnoreCase(operatorString))
            return AND;
        else if(OR.operatorString.equalsIgnoreCase(operatorString))
            return OR;
        else if (NOT.operatorString.equalsIgnoreCase(operatorString))
            return NOT;
        else
            throw new RuntimeException("Failed to parse operator " + operatorString);
    }
}
