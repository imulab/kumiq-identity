package com.kumiq.identity.scim.path

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class PathTokenTests {

    @Test
    void testPathTokenAppending() {
        PathToken first = createSimpleTokenList()

        Assert.assertTrue(first.isRoot())
        Assert.assertFalse(first.isLeaf())

        Assert.assertFalse(first.next[0].isRoot())
        Assert.assertFalse(first.next[0].isLeaf())

        Assert.assertFalse(first.next[0].next[0].isRoot())
        Assert.assertTrue(first.next[0].next[0].isLeaf())
    }

    @Test
    void testPathTokenReplacement() {
        PathToken first = createSimpleTokenList()
        first.replaceTokens(first.next[0], [PathTokenFactory.simplePathToken('second-1'), PathTokenFactory.simplePathToken('second-2')])

        Assert.assertEquals(2, first.next.size())
        Assert.assertEquals('second-1', first.next[0].pathFragment())
        Assert.assertEquals('second-2', first.next[1].pathFragment())
        Assert.assertEquals('third', first.next[0].next[0].pathFragment())
        Assert.assertEquals('third', first.next[1].next[0].pathFragment())
    }

    @Test
    void testSimpleTokenListEvaluation() {
        PathToken first = createSimpleTokenList()
        Map root = [
                'first': [
                        'second': [
                                'third': 'foo'
                        ]
                ]
        ]
        PathEvaluationContext context = first.evaluate(root, root)
        Assert.assertEquals('foo', context.getValue())
    }

    @Test
    void testSimpleTokenListEvaluationExistPremature() {
        PathToken first = createSimpleTokenList()
        Map root = [
                'first': [
                        'second': [:]
                ]
        ]
        PathEvaluationContext context = first.evaluate(root, root)
        Assert.assertNull(context.getValue())
    }

    @Test
    void testHybridTokenListEvaluation() {
        PathToken first = createHybridTokenList()
        Map root = [
                'first': [
                        'second': [
                                ['third': 'foo'],
                                ['third': 'bar']
                        ]
                ]
        ]
        PathEvaluationContext context = first.evaluate(root, root)
        Assert.assertEquals('bar', context.getValue())
    }

    @Test
    void testCloneSimpleDownStream() {
        PathToken root = PathTokenFactory.root()
        PathToken first = createSimpleTokenList()
        root.appendToken(first)

        PathToken firstCloned = first.cloneSelfAndDownStream(root)
        root.replaceTokenAndDownstream(first, firstCloned)

        Assert.assertEquals(first.pathFragment(), root.firstNext().pathFragment())
        Assert.assertNotEquals(first.id, root.firstNext().id)

        Assert.assertEquals(first.firstNext().pathFragment(), root.firstNext().firstNext().pathFragment())
        Assert.assertNotEquals(first.firstNext().id, root.firstNext().firstNext().id)

        Assert.assertEquals(first.firstNext().firstNext().pathFragment(), root.firstNext().firstNext().firstNext().pathFragment())
        Assert.assertNotEquals(first.firstNext().firstNext().id, root.firstNext().firstNext().firstNext().id)
    }

    @Test
    void testCloneHybridDownStream() {
        PathToken root = PathTokenFactory.root()
        PathToken first1 = PathTokenFactory.pathWithIndex('first[1]')
        PathToken first2 = PathTokenFactory.pathWithIndex('first[2]')
        PathToken second = PathTokenFactory.simplePathToken('second')
        PathToken third = PathTokenFactory.simplePathToken('third')

        root.appendToken(first1)
        root.appendToken(first2)
        first1.appendToken(second)
        first2.appendToken(second)
        second.appendToken(third)

        String origFirst1Id = root.next[0].id
        String origFirst2Id = root.next[1].id
        String origSecondId = root.next[0].next[0].id
        String origThirdId = root.next[0].next[0].next[0].id

        root.replaceDownstreamWithClones()

        String newFirst1Id = root.next[0].id
        String newFirst2Id = root.next[1].id
        String newSecondId = root.next[0].next[0].id
        String newThirdId = root.next[0].next[0].next[0].id

        Assert.assertNotEquals(origFirst1Id, newFirst1Id)
        Assert.assertNotEquals(origFirst2Id, newFirst2Id)
        Assert.assertNotEquals(origSecondId, newSecondId)
        Assert.assertNotEquals(origThirdId, newThirdId)
    }

    @Test
    void testPathTraversal() {
        PathToken root = createTreeLike()

        List<List<PathToken>> paths = []
        root.traverse(paths)
        Assert.assertEquals('first[1]', paths[0][1].pathFragment())
        Assert.assertEquals('second', paths[0][2].pathFragment())
        Assert.assertEquals('third', paths[0][3].pathFragment())

        Assert.assertEquals('first[2]', paths[1][1].pathFragment())
        Assert.assertEquals('second', paths[1][2].pathFragment())
        Assert.assertEquals('third', paths[1][3].pathFragment())
    }

    @Test
    void testClonedSubListWithLeaf() {
        PathToken root = PathTokenFactory.root()
        root.appendToken(createSimpleTokenList())

        PathToken newRoot = root.firstNext().firstNext().clonedSubListWithSelfAsLeaf()

        Assert.assertEquals('first', newRoot.firstNext().pathFragment())
        Assert.assertEquals('second', newRoot.firstNext().firstNext().pathFragment())
        Assert.assertNull(newRoot.firstNext().firstNext().firstNext())
    }

    private static PathToken createSimpleTokenList() {
        PathToken first = PathTokenFactory.simplePathToken('first')
        PathToken second = PathTokenFactory.simplePathToken('second')
        PathToken third = PathTokenFactory.simplePathToken('third')

        first.appendToken(second)
        second.appendToken(third)

        return first;
    }

    private static PathToken createHybridTokenList() {
        PathToken first = PathTokenFactory.simplePathToken('first')
        PathToken second = PathTokenFactory.pathWithIndex('second[1]')
        PathToken third = PathTokenFactory.simplePathToken('third')

        first.appendToken(second)
        second.appendToken(third)

        return first;
    }

    private static PathToken createTreeLike() {
        PathToken root = PathTokenFactory.root()
        PathToken first1 = PathTokenFactory.pathWithIndex('first[1]')
        PathToken first2 = PathTokenFactory.pathWithIndex('first[2]')
        PathToken second = PathTokenFactory.simplePathToken('second')
        PathToken third = PathTokenFactory.simplePathToken('third')

        root.appendToken(first1)
        root.appendToken(first2)
        first1.appendToken(second)
        first2.appendToken(second)
        second.appendToken(third)

        return root;
    }
}
