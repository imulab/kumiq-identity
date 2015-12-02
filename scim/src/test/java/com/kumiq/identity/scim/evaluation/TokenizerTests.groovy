package com.kumiq.identity.scim.evaluation

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
class TokenizerTests {

    @Test
    void testPathTokenizer() {
        Tokenizer tokenizer = new Tokenizer.PathTokenizer('foo.bar[x eq 100].foobar')

        Assert.assertEquals('foo', tokenizer.nextSequence())
        Assert.assertEquals('bar[x eq 100]', tokenizer.nextSequence())
        Assert.assertEquals('foobar', tokenizer.nextSequence())

        try {
            tokenizer.nextSequence()
            Assert.fail('tokenizer should have failed after being exhausted')
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof Tokenizer.NoMoreSequenceException)
        }
    }

    @Test
    void testFilterTokenizer() {
        Tokenizer tokenizer = new Tokenizer.FilterTokenizer('[(value eq 100) and (name sw "A")]');

        Assert.assertEquals('(', tokenizer.nextSequence())
        Assert.assertEquals('value', tokenizer.nextSequence())
        Assert.assertEquals('eq', tokenizer.nextSequence())
        Assert.assertEquals('100', tokenizer.nextSequence())
        Assert.assertEquals(')', tokenizer.nextSequence())
        Assert.assertEquals('and', tokenizer.nextSequence())
        Assert.assertEquals('(', tokenizer.nextSequence())
        Assert.assertEquals('name', tokenizer.nextSequence())
        Assert.assertEquals('sw', tokenizer.nextSequence())
        Assert.assertEquals('"A"', tokenizer.nextSequence())
        Assert.assertEquals(')', tokenizer.nextSequence())

        try {
            tokenizer.nextSequence()
            Assert.fail('tokenizer should have failed after being exhausted')
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof Tokenizer.NoMoreSequenceException)
        }
    }
}
