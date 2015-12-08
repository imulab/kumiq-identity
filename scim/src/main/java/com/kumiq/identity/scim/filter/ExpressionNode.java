package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.Configuration;
import org.springframework.util.Assert;

/**
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class ExpressionNode<T> implements Predicate {

    private final String expressionValue;
    private T left;
    private T right;

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

    @Override
    public boolean isOperator() {
        return true;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public T getRight() {
        return right;
    }

    public void setRight(T right) {
        this.right = right;
    }

    /**
     * A node presenting a logical expression, usually AND, OR and NOT.
     */
    public static class LogicalExpressionNode extends ExpressionNode<Predicate> {

        private final LogicalOperator operator;

        public static LogicalExpressionNode andNode(Predicate left, Predicate right) {
            return new LogicalExpressionNode(left, LogicalOperator.AND, right);
        }

        public static LogicalExpressionNode orNode(Predicate left, Predicate right) {
            return new LogicalExpressionNode(left, LogicalOperator.OR, right);
        }

        public static LogicalExpressionNode notNode(Predicate left) {
            return new LogicalExpressionNode(left, LogicalOperator.NOT, null);
        }

        public LogicalExpressionNode(Predicate left, LogicalOperator operator, Predicate right) {
            super(operator.getOperatorString());
            Assert.isTrue(LogicalOperator.AND == operator
                    || LogicalOperator.OR == operator
                    || LogicalOperator.NOT == operator);
            setLeft(left);
            setRight(right);
            this.operator = operator;
        }

        @Override
        public boolean apply(Object data, Configuration configuration) {
            if (LogicalOperator.AND == operator)
                return Boolean.logicalAnd(getLeft().apply(data, configuration), getRight().apply(data, configuration));
            else if (LogicalOperator.OR == operator)
                return Boolean.logicalOr(getLeft().apply(data, configuration), getRight().apply(data, configuration));
            else
                return !getLeft().apply(data, configuration);
        }

        @Override
        public int precedence() {
            return this.operator.precedence();
        }

        @Override
        public Associtivity associtivity() {
            return this.operator.associtivity();
        }

        public LogicalOperator getOperator() {
            return operator;
        }
    }

    /**
     * A node presenting a relational expression, usually involves one or two {@link ValueNode} operand and a
     * relational operator.
     */
    public static class RelationalExpressionNode extends ExpressionNode<ValueNode> {

        private final RelationalOperator operator;

        public RelationalExpressionNode(ValueNode left, RelationalOperator operator, ValueNode right) {
            super(operator.getOperatorString());
            setLeft(left);
            setRight(right);
            this.operator = operator;
        }

        @Override
        public boolean apply(Object data, Configuration configuration) {
            return EvaluatorFactory
                    .createEvaluator(this.operator)
                    .evaluate(getLeft(), getRight(), data, configuration);
        }

        @Override
        public int precedence() {
            return this.operator.precedence();
        }

        @Override
        public Associtivity associtivity() {
            return this.operator.associtivity();
        }

        public RelationalOperator getOperator() {
            return operator;
        }
    }
}
