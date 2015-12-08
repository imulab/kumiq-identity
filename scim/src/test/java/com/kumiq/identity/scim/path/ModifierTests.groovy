package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.resource.misc.Schema
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
class ModifierTests {

    @Test
    void testAddSimpleAttributeWithMissingProperty() {
        Map subject = [:]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'name.lastName', 'Qiu')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'firstName', multiValued: false, clazz: String),
                                new Schema.Attribute(name: 'lastName', multiValued: false, clazz: String)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathRef pathRef = PathCompiler.compile(unit.getPath(), subject)[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Qiu', subject.get('name').get('lastName'))
    }

    @Test
    void testAddSimpleAttribute() {
        Map subject = ['name': ['firstName': 'David']]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'name.lastName', 'Qiu')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'firstName', multiValued: false, clazz: String),
                                new Schema.Attribute(name: 'lastName', multiValued: false, clazz: String)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathRef pathRef = PathCompiler.compile(unit.getPath(), subject)[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Qiu', subject.get('name').get('lastName'))
    }

    @Test
    void testRemoveSimpleAttribute() {
        Map subject = ['name': ['firstName': 'David']]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'name.firstName', null)
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'firstName', multiValued: false, clazz: String)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathRef pathRef = PathCompiler.compile(unit.getPath(), subject)[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertNull(subject.get('name').get('firstName'))
    }

    @Test
    void testReplaceSimpleAttribute() {
        Map subject = ['name': ['firstName': 'David']]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.REPLACE, 'name.firstName', 'Weinan')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'firstName', multiValued: false, clazz: String)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathRef pathRef = PathCompiler.compile(unit.getPath(), subject)[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Weinan', subject.get('name').get('firstName'))
    }

    @Test
    void testAddArrayAttribute() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': true]
        ]]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'emails', ['value': 'bar@foo.com', 'primary': false])
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathRef pathRef = PathCompiler.compile(unit.getPath(), subject)[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals(2, subject.get('emails').size())
        Assert.assertEquals('foo@bar.com', subject.get('emails')[0].get('value'))
        Assert.assertEquals('bar@foo.com', subject.get('emails')[1].get('value'))
    }

    @Test
    void testAddSimpleAttributeWithFilter() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': true, status: 'A'],
                ['value': 'bar@foo.com', 'primary': false, status: 'A'],
                ['value': 'foobar@foo.com', 'primary': false, status: 'A']
        ]]
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.ADD,
                'emails[primary eq false].status', 'B')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'status', multiValued: false)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathCompiler.compile(unit.getPath(), subject).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('A', subject.get('emails')[0].get('status'))
        Assert.assertEquals('B', subject.get('emails')[1].get('status'))
        Assert.assertEquals('B', subject.get('emails')[2].get('status'))
    }

    @Test
    void testReplaceAttributeWithFilter() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': false, status: 'A'],
                ['value': 'bar@foo.com', 'primary': true, status: 'A'],
                ['value': 'foobar@foo.com', 'primary': false, status: 'A']
        ]]
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'status', multiValued: false)
                        ]
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REPLACE,
                'emails[primary eq false].status', 'B')
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathCompiler.compile(unit.getPath(), subject).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('A', subject.get('emails')[1].get('status'))
        Assert.assertEquals('B', subject.get('emails')[0].get('status'))
        Assert.assertEquals('B', subject.get('emails')[2].get('status'))
    }

    @Test
    void testRemoveAttributeWithFilter() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': false, status: 'A'],
                ['value': 'bar@foo.com', 'primary': true, status: 'A'],
                ['value': 'foobar@foo.com', 'primary': false, status: 'A']
        ]]
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'status', multiValued: false)
                        ]
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REMOVE,
                'emails[primary eq false].status', null)
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathCompiler.compile(unit.getPath(), subject).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('A', subject.get('emails')[1].get('status'))
        Assert.assertNull(subject.get('emails')[0].get('status'))
        Assert.assertNull(subject.get('emails')[2].get('status'))
    }

    @Test
    void testReplaceArrayElementAttributeWithFilter() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': false, status: 'A'],
                ['value': 'bar@foo.com', 'primary': true, status: 'A'],
                ['value': 'foobar@foo.com', 'primary': false, status: 'A']
        ]]
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REPLACE,
                'emails[value eq "foo@bar.com"]', ['value': 'david@foo.com'])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathCompiler.compile(unit.getPath(), subject).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('david@foo.com', subject.get('emails')[0].get('value'))
        Assert.assertEquals('bar@foo.com', subject.get('emails')[1].get('value'))
        Assert.assertEquals('foobar@foo.com', subject.get('emails')[2].get('value'))
    }

    @Test
    void testRemoveArrayElementAttributeWithFilter() {
        Map subject = ['emails': [
                ['value': 'foo@bar.com', 'primary': false, status: 'A'],
                ['value': 'bar@foo.com', 'primary': true, status: 'A'],
                ['value': 'foobar@foo.com', 'primary': false, status: 'A']
        ]]
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REMOVE,
                'emails[primary eq false]', null)
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        PathCompiler.compile(unit.getPath(), subject).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(1, subject.get('emails').size())
        Assert.assertEquals('bar@foo.com', subject.get('emails')[0].get('value'))
    }
}
