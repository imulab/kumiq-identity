package com.kumiq.identity.scim.path;

import java.util.Map;

/**
 * Context information during evaluation.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class EvaluationContext {

    private final Map<String, Object> root;
    private Object value;

    public EvaluationContext(Map<String, Object> root) {
        this.root = root;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getRoot() {
        return root;
    }
}
