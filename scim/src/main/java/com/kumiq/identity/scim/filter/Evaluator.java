package com.kumiq.identity.scim.filter;

import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface Evaluator {

    boolean evaluate(ValueNode left, ValueNode right, EvaluationContext context);

    class EvaluationContext {

        private final Map<String, Object> data;

        public EvaluationContext(Map<String, Object> data) {
            this.data = data;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}
