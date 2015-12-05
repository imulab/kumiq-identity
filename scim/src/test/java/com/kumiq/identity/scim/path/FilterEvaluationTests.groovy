package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.filter.FilterCompiler
import com.kumiq.identity.scim.filter.Predicate
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
class FilterEvaluationTests {

    @Test
    void testEvaluateSimpleEq() {
        Predicate predicate = FilterCompiler.compile('user.name eq "David"');
        Map data1 = ['user': ['name': 'David']]
        Map data2 = ['user': ['name': 'Mary']]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleNe() {
        Predicate predicate = FilterCompiler.compile('enrolled ne true');
        Map data1 = ['enrolled': false]
        Map data2 = ['enrolled': true]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleCo() {
        Predicate predicate = FilterCompiler.compile('name co "Michael"');
        Map data1 = ['name': 'Michael Jordan']
        Map data2 = ['name': 'Kobe Bryant']

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleSw() {
        Predicate predicate = FilterCompiler.compile('name sw "Michael"');
        Map data1 = ['name': 'Michael Jordan']
        Map data2 = ['name': 'Kobe Bryant']

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleEw() {
        Predicate predicate = FilterCompiler.compile('name ew "Jordan"');
        Map data1 = ['name': 'Michael Jordan']
        Map data2 = ['name': 'Kobe Bryant']

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimplePr() {
        Predicate predicate = FilterCompiler.compile('name pr');
        Map data1 = ['name': 'Michael Jordan']
        Map data2 = [:]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))

        Predicate predicate2 = FilterCompiler.compile('emails pr');
        Map data3 = ['emails': []]
        Map data4 = ['emails': null]
        Assert.assertFalse(predicate2.apply(data3))
        Assert.assertFalse(predicate2.apply(data4))
    }

    @Test
    void testEvaluateSimpleGtNumber() {
        Predicate predicate = FilterCompiler.compile('age gt 10')
        Map data1 = ['age': 11]
        Map data2 = ['age': 10]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleGtDate() {
        Predicate predicate = FilterCompiler.compile('created gt "2013-12-05T06:35:44.121Z"')
        Map data1 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2015-12-05T06:35:44.121Z')]
        Map data2 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2013-12-05T06:35:44.121Z')]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleGeNumber() {
        Predicate predicate = FilterCompiler.compile('age ge 10')
        Map data1 = ['age': 11]
        Map data2 = ['age': 10]
        Map data3 = ['age': 9]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertFalse(predicate.apply(data3))
    }

    @Test
    void testEvaluateSimpleGeDate() {
        Predicate predicate = FilterCompiler.compile('created ge "2013-12-05T06:35:44.121Z"')
        Map data1 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2015-12-05T06:35:44.121Z')]
        Map data2 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2013-12-05T06:35:44.121Z')]
        Map data3 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2012-12-05T06:35:44.121Z')]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertFalse(predicate.apply(data3))
    }

    @Test
    void testEvaluateSimpleLtNumber() {
        Predicate predicate = FilterCompiler.compile('age lt 10')
        Map data1 = ['age': 9]
        Map data2 = ['age': 10]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleLtDate() {
        Predicate predicate = FilterCompiler.compile('created lt "2013-12-05T06:35:44.121Z"')
        Map data1 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2012-12-05T06:35:44.121Z')]
        Map data2 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2013-12-05T06:35:44.121Z')]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
    }

    @Test
    void testEvaluateSimpleLeNumber() {
        Predicate predicate = FilterCompiler.compile('age le 10')
        Map data1 = ['age': 9]
        Map data2 = ['age': 10]
        Map data3 = ['age': 11]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertFalse(predicate.apply(data3))
    }

    @Test
    void testEvaluateSimpleLeDate() {
        Predicate predicate = FilterCompiler.compile('created le "2013-12-05T06:35:44.121Z"')
        Map data1 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2012-12-05T06:35:44.121Z')]
        Map data2 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2013-12-05T06:35:44.121Z')]
        Map data3 = ['created': new SimpleDateFormat(ScimConstants.DATE_FORMAT).parse('2015-12-05T06:35:44.121Z')]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertFalse(predicate.apply(data3))
    }

    @Test
    void testEvaluateLogicalAnd() {
        Predicate predicate = FilterCompiler.compile('(age gt 10) and (active eq true)')
        Map data1 = ['age': 11, 'active': true]
        Map data2 = ['age': 11, 'active': false]
        Map data3 = ['age': 10, 'active': true]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertFalse(predicate.apply(data2))
        Assert.assertFalse(predicate.apply(data3))
    }

    @Test
    void testEvaluateLogicalOr() {
        Predicate predicate = FilterCompiler.compile('(age gt 10) or (active eq true)')
        Map data1 = ['age': 11, 'active': true]
        Map data2 = ['age': 11, 'active': false]
        Map data3 = ['age': 10, 'active': true]
        Map data4 = ['age': 9, 'active': false]

        Assert.assertTrue(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertTrue(predicate.apply(data3))
        Assert.assertFalse(predicate.apply(data4))
    }

    @Test
    void testEvaluateLogicalNot() {
        Predicate predicate = FilterCompiler.compile('not(name pr)')
        Map data1 = ['name': 'David']
        Map data2 = [:]

        Assert.assertFalse(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
    }

    @Test
    void testEvaluateComplexFilter() {
        Predicate predicate = FilterCompiler.compile('(active eq (age gt 10)) or (meta.power gt 1000)')
        Map data1 = [
                'active': true,
                'age': 9,
                'meta': ['power': 999]
        ]
        Map data2 = [
                'active': true,
                'age': 11,
                'meta': ['power': 999]
        ]
        Map data3 = [
                'active': true,
                'age': 9,
                'meta': ['power': 1001]
        ]

        Assert.assertFalse(predicate.apply(data1))
        Assert.assertTrue(predicate.apply(data2))
        Assert.assertTrue(predicate.apply(data3))
    }
}
