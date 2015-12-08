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
    void testPathTraversal() {
        PathToken root = createTreeLike()

        List<PathRef> paths = root.traverse()

        Assert.assertEquals('first[1]', paths[0].next.pathToken.pathFragment())
        Assert.assertEquals('second', paths[0].next.next.pathToken.pathFragment())
        Assert.assertEquals('third', paths[0].next.next.next.pathToken.pathFragment())

        Assert.assertEquals('first[2]', paths[1].next.pathToken.pathFragment())
        Assert.assertEquals('second', paths[1].next.next.pathToken.pathFragment())
        Assert.assertEquals('third', paths[1].next.next.next.pathToken.pathFragment())
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
