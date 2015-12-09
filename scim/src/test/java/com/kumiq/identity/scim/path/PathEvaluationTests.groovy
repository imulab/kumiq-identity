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
class PathEvaluationTests {

    @Test
    void testSimpleEvaluation() {
        Map data = ['name': ['firstName': 'David']]
        List<PathRef> paths = PathCompiler.compile(
                CompilationContext.create('name.firstName', data),
                Configuration.withMapObjectProvider())

        EvaluationContext context = new EvaluationContext(data)
        context = paths[0].evaluate(context, Configuration.withMapObjectProvider())
        Assert.assertEquals('David', context.getCursor())
    }

    @Test
    void testComplexEvaluation() {
        Map data = [
                'me': [
                        'emails': [
                                ['value': 'foo@bar.com', 'active': true],
                                ['value': 'bar@foo.com', 'active': false],
                                ['value': 'foo@gmail.com', 'active': true]
                        ]
                ]
        ]
        List<PathRef> paths = PathCompiler.compile(
                CompilationContext.create('me.emails[active eq true].value', data),
                Configuration.withMapObjectProvider())

        Assert.assertEquals(2, paths.size())
        EvaluationContext context

        context = new EvaluationContext(data)
        Assert.assertEquals('foo@gmail.com', paths[0].evaluate(context, Configuration.withMapObjectProvider()).getCursor())

        context = new EvaluationContext(data)
        Assert.assertEquals('foo@bar.com', paths[1].evaluate(context, Configuration.withMapObjectProvider()).getCursor())
    }
}
