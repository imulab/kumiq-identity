package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.filter.*
import com.kumiq.identity.scim.resource.constant.ScimConstants
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.text.SimpleDateFormat

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class FilterCompilerTests {

    @Test
    void testCompileSimpleEqPredicate() {
        Predicate predicate = FilterCompiler.compile('user.name eq "David"');

        assertRelational(predicate, RelationalOperator.EQ);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'user.name')
        assertStringNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), 'David')
    }

    @Test
    void testCompileSimpleNePredicate() {
        Predicate predicate = FilterCompiler.compile('enrolled ne true');

        assertRelational(predicate, RelationalOperator.NE);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'enrolled')
        assertBooleanNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), Boolean.TRUE)
    }

    @Test
    void testCompileSimpleCoPredicate() {
        Predicate predicate = FilterCompiler.compile('name co "Michael"');

        assertRelational(predicate, RelationalOperator.CO);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'name')
        assertStringNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), 'Michael')
    }

    @Test
    void testCompileSimpleSwPredicate() {
        Predicate predicate = FilterCompiler.compile('label sw "KMQ-"')

        assertRelational(predicate, RelationalOperator.SW);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'label')
        assertStringNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), 'KMQ-')
    }

    @Test
    void testCompileSimpleEwPredicate() {
        Predicate predicate = FilterCompiler.compile('label ew "-001"')

        assertRelational(predicate, RelationalOperator.EW);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'label')
        assertStringNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), '-001')
    }

    @Test
    void testCompileSimplePrPredicate() {
        Predicate predicate = FilterCompiler.compile('description pr')

        assertRelational(predicate, RelationalOperator.PR);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'description')
        assertNullNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight())
    }

    @Test
    void testCompileSimpleGtPredicateWithNumber() {
        Predicate predicate = FilterCompiler.compile('age gt 10')

        assertRelational(predicate, RelationalOperator.GT);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'age')
        assertNumberNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), new BigDecimal('10'))
    }

    @Test
    void testCompileSimpleGtPredicateWithDate() {
        Predicate predicate = FilterCompiler.compile('created gt "2013-12-05T06:35:44.121Z"')

        assertRelational(predicate, RelationalOperator.GT);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'created')
        assertDateNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), '2013-12-05T06:35:44.121Z')
    }

    @Test
    void testCompileSimpleGePredicate() {
        Predicate predicate = FilterCompiler.compile('age ge 10')

        assertRelational(predicate, RelationalOperator.GE);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'age')
        assertNumberNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), new BigDecimal('10'))
    }

    @Test
    void testCompileSimpleLtPredicate() {
        Predicate predicate = FilterCompiler.compile('age lt 10')

        assertRelational(predicate, RelationalOperator.LT);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'age')
        assertNumberNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), new BigDecimal('10'))
    }

    @Test
    void testCompileSimpleLePredicate() {
        Predicate predicate = FilterCompiler.compile('age le 10')

        assertRelational(predicate, RelationalOperator.LE);
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'age')
        assertNumberNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), new BigDecimal('10'))
    }

    @Test
    void testCompileLogicalAndPredicate() {
        Predicate predicate = FilterCompiler.compile('(age gt 10) and (name eq "David")')

        assertLogical(predicate, LogicalOperator.AND)
        assertRelational(((ExpressionNode.LogicalExpressionNode) predicate).getLeft(), RelationalOperator.GT)
        assertRelational(((ExpressionNode.LogicalExpressionNode) predicate).getRight(), RelationalOperator.EQ)

        ExpressionNode.RelationalExpressionNode gtNode = ((ExpressionNode.LogicalExpressionNode) predicate).getLeft()
        assertPathNode(gtNode.getLeft(), 'age')
        assertNumberNode(gtNode.getRight(), new BigDecimal('10'))

        ExpressionNode.RelationalExpressionNode eqNode = ((ExpressionNode.LogicalExpressionNode) predicate).getRight()
        assertPathNode(eqNode.getLeft(), 'name')
        assertStringNode(eqNode.getRight(), 'David')
    }

    @Test
    void testCompileLogicalOrPredicate() {
        Predicate predicate = FilterCompiler.compile('(age gt 10) or (name eq "David")')

        assertLogical(predicate, LogicalOperator.OR)
        assertRelational(((ExpressionNode.LogicalExpressionNode) predicate).getLeft(), RelationalOperator.GT)
        assertRelational(((ExpressionNode.LogicalExpressionNode) predicate).getRight(), RelationalOperator.EQ)
    }

    @Test
    void testCompileLogicalNotPredicate() {
        Predicate predicate = FilterCompiler.compile('not(name sw "David") and (age gt 10)')

        assertLogical(predicate, LogicalOperator.AND)
        assertLogical(((ExpressionNode.LogicalExpressionNode) predicate).getLeft(), LogicalOperator.NOT)
        assertRelational(((ExpressionNode.LogicalExpressionNode) predicate).getRight(), RelationalOperator.GT)

        ExpressionNode.LogicalExpressionNode notNode = ((ExpressionNode.LogicalExpressionNode) predicate).getLeft()
        assertRelational(notNode.getLeft(), RelationalOperator.SW)
        Assert.assertTrue(notNode.getRight() instanceof ValueNode.NullNode)
    }

    @Test
    void testCompileNestedLogicalNotPredicate() {
        Predicate predicate = FilterCompiler.compile('active eq not(tags co "deleted")')

        assertRelational(predicate, RelationalOperator.EQ)
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'active')
        assertPredicateNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), LogicalOperator.NOT)
    }

    @Test
    void testCompileNestedLogicalAndPredicate() {
        Predicate predicate = FilterCompiler.compile('active eq ((age gt 10) and (name sw "David"))')

        assertRelational(predicate, RelationalOperator.EQ)
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'active')
        assertPredicateNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), LogicalOperator.AND)
    }

    @Test
    void testCompiledNestedRelationalPredicate() {
        Predicate predicate = FilterCompiler.compile('active eq (age gt 10)')

        assertRelational(predicate, RelationalOperator.EQ)
        assertPathNode(((ExpressionNode.RelationalExpressionNode) predicate).getLeft(), 'active')
        assertPredicateNode(((ExpressionNode.RelationalExpressionNode) predicate).getRight(), RelationalOperator.GT)
    }

    private static void assertLogical(Predicate predicate, LogicalOperator operator) {
        Assert.assertTrue(predicate instanceof ExpressionNode.LogicalExpressionNode)
        Assert.assertEquals(operator, ((ExpressionNode.LogicalExpressionNode) predicate).getOperator())
    }

    private static void assertRelational(Predicate predicate, RelationalOperator operator) {
        Assert.assertTrue(predicate instanceof ExpressionNode.RelationalExpressionNode)
        Assert.assertEquals(operator, ((ExpressionNode.RelationalExpressionNode) predicate).getOperator())
    }

    private static void assertPathNode(ValueNode node, String path) {
        Assert.assertTrue(node.isPathNode())
        Assert.assertEquals(path, node.asPathNode().getFaceValue())
    }

    private static void assertStringNode(ValueNode node, String value) {
        Assert.assertTrue(node.isStringNode())
        Assert.assertEquals(value, node.asStringNode().getValue())
    }

    private static void assertBooleanNode(ValueNode node, Boolean value) {
        Assert.assertTrue(node.isBooleanNode())
        Assert.assertEquals(value, node.asBooleanNode().getValue())
    }

    private static void assertNullNode(ValueNode node) {
        Assert.assertTrue(node.isNullNode())
    }

    private static void assertNumberNode(ValueNode node, BigDecimal number) {
        Assert.assertTrue(node.isNumberNode())
        Assert.assertEquals(0, number.compareTo(node.asNumberNode().getNumber()))
    }

    private static void assertDateNode(ValueNode node, String date) {
        Assert.assertTrue(node.isDateNode())
        Assert.assertEquals(new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse(date), node.asDateNode().getDate())
    }

    private static void assertPredicateNode(ValueNode node, LogicalOperator operator) {
        Assert.assertTrue(node.isPredicateNode())
        Predicate predicate = node.asPredicateNode().getPredicate()
        Assert.assertTrue(predicate instanceof ExpressionNode.LogicalExpressionNode)
        Assert.assertEquals(operator, ((ExpressionNode.LogicalExpressionNode) predicate).getOperator())
    }

    private static void assertPredicateNode(ValueNode node, RelationalOperator operator) {
        Assert.assertTrue(node.isPredicateNode())
        Predicate predicate = node.asPredicateNode().getPredicate()
        Assert.assertTrue(predicate instanceof ExpressionNode.RelationalExpressionNode)
        Assert.assertEquals(operator, ((ExpressionNode.RelationalExpressionNode) predicate).getOperator())
    }
}
