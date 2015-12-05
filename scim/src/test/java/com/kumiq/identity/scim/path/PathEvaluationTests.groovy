package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.filter.Predicate
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
        List<PathRef> paths = PathCompiler.compile('name.firstName', data)

        PathEvaluationContext context = paths[0].evaluate(data, data)
        Assert.assertEquals('David', context.getValue())
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
        List<PathRef> paths = PathCompiler.compile('me.emails[active eq true].value', data)

        Assert.assertEquals(2, paths.size())
        Assert.assertEquals('foo@bar.com', paths[0].evaluate(data, data).getValue())
        Assert.assertEquals('foo@gmail.com', paths[1].evaluate(data, data).getValue())
    }
}
