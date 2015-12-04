package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public enum RelationalOperator implements OperatorAware {

    EQ(ScimConstants.FILTER_EQUAL, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    NE(ScimConstants.FILTER_NOT_EQUAL, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    CO(ScimConstants.FILTER_CONTAINS, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    SW(ScimConstants.FILTER_STARTS_WITH, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    EW(ScimConstants.FILTER_ENDS_WITH, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    PR(ScimConstants.FILTER_PRESENT, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    GT(ScimConstants.FILTER_GREATER_THAN, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    GE(ScimConstants.FILTER_GREATER_EQUAL, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    LT(ScimConstants.FILTER_LESS_THAN, DEFAULT_PRECEDENCE, Associtivity.LEFT),
    LE(ScimConstants.FILTER_LESS_EQUAL, DEFAULT_PRECEDENCE, Associtivity.LEFT);

    private final String operatorString;
    private final int precedence;
    private final Associtivity associtivity;

    RelationalOperator(String operatorString, int precedence, Associtivity associtivity) {
        this.operatorString = operatorString;
        this.precedence = precedence;
        this.associtivity = associtivity;
    }

    @Override
    public int precedence() {
        return this.precedence;
    }

    @Override
    public Associtivity associtivity() {
        return this.associtivity;
    }

    public static RelationalOperator fromString(String operatorString){
        for (RelationalOperator operator : RelationalOperator.values()) {
            if(operator.operatorString.equals(operatorString.toUpperCase()) ){
                return operator;
            }
        }
        throw new RuntimeException("Operator " + operatorString + " not supported ");
    }

    public String getOperatorString() {
        return operatorString;
    }

    @Override
    public String toString() {
        return this.operatorString;
    }
}
