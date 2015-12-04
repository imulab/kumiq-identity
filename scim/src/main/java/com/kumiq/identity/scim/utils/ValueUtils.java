package com.kumiq.identity.scim.utils;

import com.kumiq.identity.scim.resource.constant.ScimConstants;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ValueUtils {

    public static String asScimString(String value) {
        return "\"" + stripQuotes(value) + "\"";
    }

    public static String asScimDate(Date date) {
        return "\"" + new SimpleDateFormat(ScimConstants.DATE_FORMAT).format(date) + "\"";
    }

    public static String asScimBoolean(Boolean value) {
        return value.toString();
    }

    public static String asScimNumber(Object value) {
        Assert.isTrue(TypeUtils.isNumber(value));
        return value.toString();
    }

    public static boolean valueIsBoolean(String value) {
        return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
    }

    public static Boolean valueAsBoolean(String value) {
        return Boolean.valueOf(value);
    }

    public static boolean valueIsNumber(String value) {
        return TypeUtils.isNumber(value);
    }

    public static BigDecimal valueAsNumber(String value) {
        return TypeUtils.asNumber(value);
    }

    public static boolean valueIsDate(String value) {
        try {
            new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse(stripQuotes(value));
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public static Date valueAsDate(String value) {
        try {
            return new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse(stripQuotes(value));
        } catch (ParseException ex) {
            return null;
        }
    }

    public static String stripQuotes(String quotedValue) {
        while (quotedValue.startsWith("\""))
            quotedValue = quotedValue.substring(1);
        while (quotedValue.endsWith("\""))
            quotedValue = quotedValue.substring(0, quotedValue.length() - 1);
        return quotedValue;
    }

    public static String stripSquareBrackets(String value) {
        while (value.startsWith("["))
            value = value.substring(1);
        while (value.endsWith("]"))
            value = value.substring(0, value.length() - 1);
        return value;
    }

    public static boolean isQuoted(String value) {
        return value.startsWith("\"") || value.endsWith("\"");
    }
}
