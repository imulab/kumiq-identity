package com.kumiq.identity.scim.filter;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface FilterToken {

    /**
     * String based token value as seen in filter expression.
     *
     * @return
     */
    String faceValue();
}
