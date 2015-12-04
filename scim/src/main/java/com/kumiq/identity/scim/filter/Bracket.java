package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public enum Bracket implements FilterToken {

    LEFT(ScimConstants.FILTER_LEFT_BRACKET),
    RIGHT(ScimConstants.FILTER_RIGHT_BRACKET);

    private final String value;

    Bracket(String value) {
        this.value = value;
    }

    @Override
    public String faceValue() {
        return this.value;
    }

    @Override
    public boolean isParenthesis() {
        return true;
    }

    public static Bracket fromString(String value) {
        if (LEFT.value.equals(value))
            return LEFT;
        else if (RIGHT.value.equals(value))
            return RIGHT;
        else
            throw new RuntimeException(value + " cannot be parsed as bracket");
    }
}
