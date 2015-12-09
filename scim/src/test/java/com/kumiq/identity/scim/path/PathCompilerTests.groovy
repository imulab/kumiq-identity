package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.utils.ExceptionFactory.PathCompiledToVoidException
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
class PathCompilerTests {

    @Test
    void testCompileSimplePath() {
        Map data = [
                'name': [
                        'firstName': 'David'
                ]
        ]
        List<PathRef> paths = PathCompiler.compile(
                CompilationContext.create('name.firstName', data),
                Configuration.withMapObjectProvider())

        Assert.assertTrue(paths.size() == 1)
        Assert.assertEquals('name', paths[0].next.pathToken.pathFragment())
        Assert.assertEquals('firstName', paths[0].next.next.pathToken.pathFragment())
    }

    @Test
    void testCompileFilterPath() {
        Map data = [
                'attributes': [
                        'emails': [
                                ['value': 'foo@bar.com', 'primary': true],
                                ['value': 'foobar@bar.com', 'primary': true],
                                ['value': 'foo@bar.com', 'primary': false]
                        ]
                ]
        ]
        List<PathRef> paths = PathCompiler.compile(
                CompilationContext.create('attributes.emails[value eq "foo@bar.com"].primary', data),
                Configuration.withMapObjectProvider()
        )

        Assert.assertTrue(paths.size() == 2)
        Assert.assertEquals('attributes', ((SimplePathToken) paths[0].next.pathToken).pathFragment())
        Assert.assertEquals('emails', ((PathWithIndexToken) paths[0].next.next.pathToken).pathComponent)
        Assert.assertEquals(2, ((PathWithIndexToken) paths[0].next.next.pathToken).indexComponent)
        Assert.assertEquals('primary', ((SimplePathToken) paths[0].next.next.next.pathToken).pathFragment())

        Assert.assertEquals('attributes', ((SimplePathToken) paths[1].next.pathToken).pathFragment())
        Assert.assertEquals('emails', ((PathWithIndexToken) paths[1].next.next.pathToken).pathComponent)
        Assert.assertEquals(0, ((PathWithIndexToken) paths[1].next.next.pathToken).indexComponent)
        Assert.assertEquals('primary', ((SimplePathToken) paths[1].next.next.next.pathToken).pathFragment())
    }

    @Test
    void testCompileFilterToNothing() {
        Map data = [
                'attributes': [
                        'emails': [
                                ['value': 'foo@bar.com', 'primary': true],
                                ['value': 'foobar@bar.com', 'primary': true],
                                ['value': 'foo@bar.com', 'primary': false]
                        ]
                ]
        ]
        try {
            PathCompiler.compile(
                    CompilationContext.create('attributes.emails[value eq "nothing"].primary', data),
                    Configuration.withMapObjectProvider()
            )
            Assert.fail('Should have thrown exception')
        } catch (PathCompiledToVoidException ex) {
            Assert.assertEquals('attributes.emails[value eq "nothing"].primary', ex.compilePath)
            Assert.assertEquals('emails[value eq "nothing"]', ex.voidPath)
        }
    }
}
