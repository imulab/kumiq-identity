package com.kumiq.identity.scim.filter;

import com.kumiq.identity.scim.path.PathCompiler;
import com.kumiq.identity.scim.path.PathEvaluationContext;
import com.kumiq.identity.scim.path.PathToken;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.utils.ValueUtils;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A node that represents a value.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class ValueNode implements FilterToken {

    private final String faceValue;

    protected ValueNode(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getFaceValue() {
        return faceValue;
    }

    @Override
    public String faceValue() {
        return getFaceValue();
    }

    public boolean isStringNode() {
        return false;
    }

    public StringNode asStringNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isDateNode() {
        return false;
    }

    public DateNode asDateNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isNumberNode() {
        return false;
    }

    public NumberNode asNumberNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isBooleanNode() {
        return false;
    }

    public BooleanNode asBooleanNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isNullNode() {
        return false;
    }

    public NullNode asNullNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isPredicateNode() {
        return false;
    }

    public PredicateNode asPredicateNode() {
        throw new RuntimeException("not supported");
    }

    public boolean isPathNode() {
        return false;
    }

    public PathNode asPathNode() {
        throw new RuntimeException("not supported");
    }

    /**
     * Value node holding a {@link String}
     */
    public static class StringNode extends ValueNode {

        private final String value;

        public StringNode(String faceValue) {
            super(faceValue);
            this.value = ValueUtils.stripQuotes(faceValue);
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean isStringNode() {
            return true;
        }

        @Override
        public StringNode asStringNode() {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof StringNode)) return false;

            StringNode that = (StringNode) obj;
            return !(value != null ? !value.equals(that.value) : that.value != null);
        }
    }

    /**
     * Value node holding a {@link Date}
     */
    public static class DateNode extends ValueNode {

        private static final DateFormat dateFormat = new SimpleDateFormat(ScimConstants.DATE_FORMAT);

        private final Date date;

        public DateNode(String faceValue) throws ParseException {
            super(faceValue);
            this.date = dateFormat.parse(ValueUtils.stripQuotes(faceValue));
        }

        public Date getDate() {
            return date;
        }

        @Override
        public boolean isDateNode() {
            return true;
        }

        @Override
        public DateNode asDateNode() {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof DateNode)) return false;

            DateNode that = (DateNode) obj;
            return this.date != null && that.date != null && this.date.equals(that.date);
        }
    }

    /**
     * Value node holding a {@link BigDecimal}
     */
    public static class NumberNode extends ValueNode {

        private final BigDecimal number;

        public NumberNode(String faceValue) {
            super(faceValue);
            this.number = new BigDecimal(faceValue);
        }

        public BigDecimal getNumber() {
            return number;
        }

        @Override
        public boolean isNumberNode() {
            return true;
        }

        @Override
        public NumberNode asNumberNode() {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof NumberNode)) return false;

            NumberNode that = (NumberNode) obj;
            return this.number != null && that.number != null && this.number.compareTo(that.number) == 0;
        }
    }

    /**
     * Value node holding a {@link Boolean}
     */
    public static class BooleanNode extends ValueNode {

        private final Boolean value;

        public BooleanNode(String faceValue) {
            super(faceValue);
            this.value = Boolean.valueOf(faceValue);
        }

        public Boolean getValue() {
            return value;
        }

        @Override
        public boolean isBooleanNode() {
            return true;
        }

        @Override
        public BooleanNode asBooleanNode() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BooleanNode)) return false;

            BooleanNode that = (BooleanNode) o;

            return !(value != null ? !value.equals(that.value) : that.value != null);
        }
    }

    /**
     * A null value node
     */
    public static class NullNode extends ValueNode {

        public NullNode() {
            super(null);
        }

        @Override
        public boolean isNullNode() {
            return true;
        }

        @Override
        public NullNode asNullNode() {
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof NullNode)) return false;

            return true;
        }
    }

    /**
     * A value node presenting further predicates (logical or relational)
     */
    public static class PredicateNode extends ValueNode {

        private final Predicate predicate;

        public PredicateNode(Predicate predicate) {
            super(predicate.toString());
            this.predicate = predicate;
        }

        public BooleanNode evaluate(Map<String, Object> data) {
            Boolean result = predicate.apply(data);
            return ValueNodeFactory.booleanNode(result.toString());
        }

        public Predicate getPredicate() {
            return predicate;
        }

        @Override
        public boolean isPredicateNode() {
            return true;
        }

        @Override
        public PredicateNode asPredicateNode() {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    /**
     * A value node presenting a SCIM path.
     */
    public static class PathNode extends ValueNode {
        private final PathCompiler compiler = new PathCompiler();
        private final PathToken pathHead;

        public PathNode(String faceValue) {
            super(faceValue);
            List<PathToken> compiledPaths = compiler.compile(faceValue, null);
            Assert.isTrue(compiledPaths.size() == 1, "Path node path should just be a simple path");
            this.pathHead = compiledPaths.get(0);
        }

        public PathToken getPathHead() {
            return pathHead;
        }

        public ValueNode evaluate(Map<String, Object> data) {
            PathEvaluationContext context = this.pathHead.evaluate(data, data);
            Object value = context.getValue();

            if (value == null)
                return ValueNodeFactory.nullNode();

            if (TypeUtils.isString(value)) {
                return ValueNodeFactory.stringNode(ValueUtils.asScimString(TypeUtils.asString(value)));
            } else if (TypeUtils.isDate(value)) {
                return ValueNodeFactory.dateNode(ValueUtils.asScimDate(TypeUtils.asDate(value)));
            } else if (TypeUtils.isNumber(value)) {
                return ValueNodeFactory.numberNode(ValueUtils.asScimNumber(value));
            } else if (TypeUtils.isBoolean(value)) {
                return ValueNodeFactory.booleanNode(ValueUtils.asScimBoolean(TypeUtils.asBoolean(value)));
            } else {
                throw new RuntimeException(value.toString() + " is not a supported value node");
            }
        }

        @Override
        public boolean isPathNode() {
            return true;
        }

        @Override
        public PathNode asPathNode() {
            return this;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
