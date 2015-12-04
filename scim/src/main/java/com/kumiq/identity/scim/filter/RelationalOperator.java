package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public enum RelationalOperator {

    EQ(ScimConstants.FILTER_EQUAL),
    NE(ScimConstants.FILTER_NOT_EQUAL),
    CO(ScimConstants.FILTER_CONTAINS),
    SW(ScimConstants.FILTER_STARTS_WITH),
    EW(ScimConstants.FILTER_ENDS_WITH),
    PR(ScimConstants.FILTER_PRESENT),
    GT(ScimConstants.FILTER_GREATER_THAN),
    GE(ScimConstants.FILTER_GREATER_EQUAL),
    LT(ScimConstants.FILTER_LESS_THAN),
    LE(ScimConstants.FILTER_LESS_EQUAL);

    private final String operatorString;

    RelationalOperator(String operatorString) {
        this.operatorString = operatorString;
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
