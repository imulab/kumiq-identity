package com.kumiq.identity.scim.filter;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface Predicate extends FilterToken {

    boolean apply(Map<String, Object> data);
}
