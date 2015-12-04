package com.kumiq.identity.scim.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class TypeUtils {

    public static boolean isInteger(Object object) {
        return object instanceof Integer;
    }

    public static boolean isDecimal(Object object) {
        return (object instanceof Float) ||
                (object instanceof Double) ||
                (object instanceof BigDecimal);
    }

    public static boolean isNumber(Object object) {
        try {
            new BigDecimal(object.toString());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isBoolean(Object object) {
        return object instanceof Boolean;
    }

    public static boolean isString(Object object) {
        return object instanceof String;
    }

    public static boolean isDate(Object object) {
        return object instanceof Date;
    }

    public static boolean isMap(Object object) {
        return object instanceof Map;
    }

    public static boolean isList(Object object) {
        return object instanceof List;
    }

    public static boolean isCollection(Object object) {
        return object instanceof Collection;
    }

    public static boolean isComplex(Object object) {
        return !isInteger(object) &&
                !isDecimal(object) &&
                !isBoolean(object) &&
                !isString(object) &&
                !isDate(object);
    }

    public static Integer asInteger(Object object) {
        if (!isInteger(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to integer");
        return (Integer) object;
    }

    public static BigDecimal asNumber(Object object) {
        if (!isNumber(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to big decimal");
        return new BigDecimal(object.toString());
    }

    public static Boolean asBoolean(Object object) {
        if (!isBoolean(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to boolean");
        return (Boolean) object;
    }

    public static String asString(Object object) {
        if (!isString(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to string");
        return (String) object;
    }

    public static Date asDate(Object object) {
        if (!isDate(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to date");
        return (Date) object;
    }

    public static Map asMap(Object object) {
        if (!isMap(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to map");
        return (Map) object;
    }

    public static List asList(Object object) {
        if (!isList(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to list");
        return (List) object;
    }

    public static Collection asCollection(Object object) {
        if (!isCollection(object))
            throw new IllegalArgumentException(object.toString() + " cannot be cast to collection");
        return (Collection) object;
    }
}
