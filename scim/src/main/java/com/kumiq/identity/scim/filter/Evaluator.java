package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.Configuration;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface Evaluator {

    boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration);
}
