package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public enum LogicalOperator {

    AND(ScimConstants.FILTER_AND),
    OR(ScimConstants.FILTER_OR),
    NOT(ScimConstants.FILTER_NOT);

    private final String operatorString;

    LogicalOperator(String operatorString) {
        this.operatorString = operatorString;
    }

    public String getOperatorString() {
        return operatorString;
    }

    @Override
    public String toString() {
        return operatorString;
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
