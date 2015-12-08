package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.Configuration;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface Predicate extends FilterToken, OperatorAware {

    boolean apply(Object data, Configuration configuration);

    default boolean apply(Map<String, Object> data) {
        return apply(data, Configuration.withMapObjectProvider());
    }
}
