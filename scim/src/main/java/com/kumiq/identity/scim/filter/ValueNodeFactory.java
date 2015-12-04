package com.kumiq.identity.scim.filter;

import java.text.ParseException;

import static com.kumiq.identity.scim.utils.ValueUtils.*;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ValueNodeFactory {

    public static ValueNode nodeFromFilterToken(String token) {
        if (isQuoted(token)) {
            if (valueIsDate(token))
                return dateNode(token);
            else
                return stringNode(token);
        } else if (valueIsBoolean(token)) {
            return booleanNode(token);
        } else if (valueIsNumber(token)) {
            return numberNode(token);
        } else {
            return pathNode(token);
        }
    }

    public static ValueNode.StringNode stringNode(String value) {
        return new ValueNode.StringNode(value);
    }

    public static ValueNode.BooleanNode booleanNode(String value) {
        return new ValueNode.BooleanNode(value);
    }

    public static ValueNode.DateNode dateNode(String value) {
        try {
            return new ValueNode.DateNode(value);
        } catch (ParseException ex) {
            throw new IllegalArgumentException(value + " is not a valid date");
        }
    }

    public static ValueNode.NumberNode numberNode(String value) {
        return new ValueNode.NumberNode(value);
    }

    public static ValueNode.NullNode nullNode() {
        return new ValueNode.NullNode();
    }

    public static ValueNode.PathNode pathNode(String value) {
        return new ValueNode.PathNode(value);
    }

    public static ValueNode.ObjectNode objectNode(Object value) {
        return new ValueNode.ObjectNode(value);
    }

    public static ValueNode.PredicateNode predicateNode(Predicate predicate) {
        return new ValueNode.PredicateNode(predicate);
    }
}
