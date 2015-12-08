package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.Configuration;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class EvaluatorFactory {

    private static final Map<RelationalOperator, Evaluator> evaluators = new HashMap<>();

    static {
        evaluators.put(RelationalOperator.EQ, new EqualEvaluator());
        evaluators.put(RelationalOperator.NE, new NotEqualEvaluator());
        evaluators.put(RelationalOperator.CO, new ContainsEvaluator());
        evaluators.put(RelationalOperator.SW, new StartsWithEvaluator());
        evaluators.put(RelationalOperator.EW, new EndsWithEvaluator());
        evaluators.put(RelationalOperator.PR, new PresentsEvaluator());
        evaluators.put(RelationalOperator.GT, new GreaterThanEvaluator());
        evaluators.put(RelationalOperator.GE, new GreaterOrEqualEvaluator());
        evaluators.put(RelationalOperator.LT, new LessThanEvaluator());
        evaluators.put(RelationalOperator.LE, new LessOrEqualEvaluator());
    }

    public static Evaluator createEvaluator(RelationalOperator operator) {
        Evaluator evaluator = evaluators.get(operator);
        if (evaluator == null)
            throw new RuntimeException(operator + " does not have a registered evaluator");
        return evaluator;
    }

    /**
     * Evaluator for "eq"
     */
    public static class EqualEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);
            else if (left.isPredicateNode())
                leftEvaluated = left.asPredicateNode().evaluate(data, configuration);

            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(data, configuration);
            else if (right.isPredicateNode())
                rightEvaluated = right.asPredicateNode().evaluate(data, configuration);

            return leftEvaluated.equals(rightEvaluated);
        }
    }

    /**
     * Evaluator for "ne"
     */
    public static class NotEqualEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            return !EvaluatorFactory.createEvaluator(RelationalOperator.EQ).evaluate(left, right, data, configuration);
        }
    }

    /**
     * Evaluator for "co"
     */
    public static class ContainsEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);
            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(data, configuration);

            Assert.isTrue(leftEvaluated.isStringNode(), "left is not string node in contains evaluator");
            Assert.isTrue(rightEvaluated.isStringNode(), "right is not string node in contains evaluator");

            return leftEvaluated.asStringNode().contains(rightEvaluated.asStringNode());
        }
    }

    /**
     * Evaluator for "sw"
     */
    public static class StartsWithEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);
            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(data, configuration);

            Assert.isTrue(leftEvaluated.isStringNode(), "left is not string node in starts with evaluator");
            Assert.isTrue(rightEvaluated.isStringNode(), "right is not string node in starts with evaluator");

            return leftEvaluated.asStringNode().startsWith(rightEvaluated.asStringNode());
        }
    }

    /**
     * Evaluator for "ew"
     */
    public static class EndsWithEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);
            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(data, configuration);

            Assert.isTrue(leftEvaluated.isStringNode(), "left is not string node in ends with evaluator");
            Assert.isTrue(rightEvaluated.isStringNode(), "right is not string node in ends with evaluator");

            return leftEvaluated.asStringNode().endsWith(rightEvaluated.asStringNode());
        }
    }

    /**
     * Evaluator for "pr"
     */
    public static class PresentsEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);

            return leftEvaluated.isValuePresent();
        }
    }

    /**
     * Common evaluator for "gt", "ge", "lt", "le"
     */
    public static abstract class CompareToEvaluator implements Evaluator {
        @Override
        public boolean evaluate(ValueNode left, ValueNode right, Object data, Configuration configuration) {
            ValueNode leftEvaluated = left;
            ValueNode rightEvaluated = right;

            if (left.isPathNode())
                leftEvaluated = left.asPathNode().evaluate(data, configuration);
            if (right.isPathNode())
                rightEvaluated = right.asPathNode().evaluate(data, configuration);

            int result = leftEvaluated.compareTo(rightEvaluated);
            return evaluateInternal(result);
        }

        abstract boolean evaluateInternal(int result);
    }

    /**
     * Evaluator for "gt"
     */
    public static class GreaterThanEvaluator extends CompareToEvaluator {
        @Override
        boolean evaluateInternal(int result) {
            return result == 1;
        }
    }

    /**
     * Evaluator for "ge"
     */
    public static class GreaterOrEqualEvaluator extends CompareToEvaluator {
        @Override
        boolean evaluateInternal(int result) {
            return result == 1 || result == 0;
        }
    }

    /**
     * Evaluator for "lt"
     */
    public static class LessThanEvaluator extends CompareToEvaluator {
        @Override
        boolean evaluateInternal(int result) {
            return result == -1;
        }
    }

    /**
     * Evaluator for "le"
     */
    public static class LessOrEqualEvaluator extends CompareToEvaluator {
        @Override
        boolean evaluateInternal(int result) {
            return result == -1 || result == 0;
        }
    }
}
