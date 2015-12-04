package com.kumiq.identity.scim.filter;

import org.springframework.util.Assert;

import java.util.Map;

/**
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class ExpressionNode implements Predicate {

    private final String expressionValue;

    protected ExpressionNode(String expressionValue) {
        this.expressionValue = expressionValue;
    }

    public String getExpressionValue() {
        return expressionValue;
    }

    @Override
    public String faceValue() {
        return this.getExpressionValue();
    }

    /**
     * A node presenting a logical expression, usually AND, OR and NOT.
     */
    public static class LogicalExpressionNode extends ExpressionNode {

        private final LogicalOperator operator;
        private final LogicalExpressionNode left;
        private final LogicalExpressionNode right;

        public static LogicalExpressionNode andNode(LogicalExpressionNode left, LogicalExpressionNode right) {
            return new LogicalExpressionNode(left, LogicalOperator.AND, right);
        }

        public static LogicalExpressionNode orNode(LogicalExpressionNode left, LogicalExpressionNode right) {
            return new LogicalExpressionNode(left, LogicalOperator.OR, right);
        }

        public static LogicalExpressionNode notNode(LogicalExpressionNode left) {
            return new LogicalExpressionNode(left, LogicalOperator.NOT, null);
        }

        public LogicalExpressionNode(LogicalExpressionNode left, LogicalOperator operator, LogicalExpressionNode right) {
            super(operator.getOperatorString());
            Assert.isTrue(LogicalOperator.AND == operator
                    || LogicalOperator.OR == operator
                    || LogicalOperator.NOT == operator);
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public boolean apply(Map<String, Object> data) {
            if (LogicalOperator.AND == operator)
                return Boolean.logicalAnd(left.apply(data), right.apply(data));
            else if (LogicalOperator.OR == operator)
                return Boolean.logicalOr(left.apply(data), right.apply(data));
            else
                return !left.apply(data);       // TODO confirm is left child instead of right child
        }

        public LogicalOperator getOperator() {
            return operator;
        }

        public LogicalExpressionNode getLeft() {
            return left;
        }

        public LogicalExpressionNode getRight() {
            return right;
        }
    }

    /**
     * A node presenting a relational expression, usually involves one or two {@link ValueNode} operand and a
     * relational operator.
     */
    public static class RelationalExpressionNode extends ExpressionNode {

        private final ValueNode left;
        private final ValueNode right;
        private final RelationalOperator operator;

        public RelationalExpressionNode(ValueNode left, RelationalOperator operator, ValueNode right) {
            super(operator.getOperatorString());
            this.left = left;
            this.right = right;
            this.operator = operator;
        }

        @Override
        public boolean apply(Map<String, Object> data) {
            return EvaluatorFactory
                    .createEvaluator(this.operator)
                    .evaluate(this.left, this.right, new Evaluator.EvaluationContext(data));
        }

        public ValueNode getLeft() {
            return left;
        }

        public ValueNode getRight() {
            return right;
        }

        public RelationalOperator getOperator() {
            return operator;
        }
    }
}
