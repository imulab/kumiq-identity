package com.kumiq.identity.scim.filter;

import groovy.util.Eval;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class EvaluatorFactory {

    private static final Map<RelationalOperator, Evaluator> evaluators = new HashMap<>();

    static {

    }

    public static Evaluator createEvaluator(RelationalOperator operator) {
        Evaluator evaluator = evaluators.get(operator);
        if (evaluator == null)
            throw new RuntimeException(operator + " does not have a registered evaluator");
        return evaluator;
    }

    public static class EqualEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, EvaluationContext context) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(context.getData());
            else if (left.isPredicateNode())
                leftEvaluated = left.asPredicateNode().evaluate(context.getData());

            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(context.getData());
            else if (right.isPredicateNode())
                rightEvaluated = right.asPredicateNode().evaluate(context.getData());

            return leftEvaluated.equals(rightEvaluated);
        }
    }

    public static class NotEqualEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, EvaluationContext context) {
            return !EvaluatorFactory.createEvaluator(RelationalOperator.EQ).evaluate(left, right, context);
        }
    }
}
