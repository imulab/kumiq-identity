package com.kumiq.identity.scim.filter;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface OperatorAware {

    Integer DEFAULT_PRECEDENCE = 100;

    int precedence();

    Associtivity associtivity();

    enum Associtivity {
        LEFT, RIGHT
    }
}
