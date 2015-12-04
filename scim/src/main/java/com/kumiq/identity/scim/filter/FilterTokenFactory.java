package com.kumiq.identity.scim.filter;

import static com.kumiq.identity.scim.resource.constant.ScimConstants.*;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class FilterTokenFactory {

    public static FilterToken create(String token) {
        switch (token) {
            case FILTER_LEFT_BRACKET:
            case FILTER_RIGHT_BRACKET:
                return Bracket.fromString(token);

            case FILTER_AND:
                return ExpressionNode.LogicalExpressionNode.andNode(null, null);
            case FILTER_OR:
                return ExpressionNode.LogicalExpressionNode.orNode(null, null);
            case FILTER_NOT:
                return ExpressionNode.LogicalExpressionNode.notNode(null);

            case FILTER_EQUAL:
            case FILTER_NOT_EQUAL:
            case FILTER_CONTAINS:
            case FILTER_STARTS_WITH:
            case FILTER_ENDS_WITH:
            case FILTER_PRESENT:
            case FILTER_GREATER_THAN:
            case FILTER_GREATER_EQUAL:
            case FILTER_LESS_THAN:
            case FILTER_LESS_EQUAL:
                return new ExpressionNode.RelationalExpressionNode(null, RelationalOperator.fromString(token), null);

            default:
                return ValueNodeFactory.nodeFromFilterToken(token);
        }
    }
}
