package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.utils.ValueUtils;

import java.text.ParseException;

import static com.kumiq.identity.scim.utils.ValueUtils.*;
import static com.kumiq.identity.scim.utils.ValueUtils.isQuoted;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ValueNodeFactory {

    public static ValueNode node(Object object) {
        if (object == null)
            return nullNode();

        String value = object.toString();
        if (isQuoted(value)) {
            if (valueIsDate(stripQuotes(value)))
                return dateNode(value);
            else
                return stringNode(value);
        } else if (valueIsBoolean(value))
            return booleanNode(value);
        else if (valueIsNumber(value))
            return numberNode(value);
        else
            return pathNode(value);
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
}
