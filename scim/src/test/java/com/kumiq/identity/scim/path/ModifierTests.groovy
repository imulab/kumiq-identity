package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import org.apache.commons.collections.CollectionUtils
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
    void testAddSimpleAttributeToMissingProperty() {
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider())[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Qiu', subject.get('name').get('lastName'))
    }

    @Test
    void testAddSimpleAttributeToMissingPropertyWithResourceObjectProvider() {
        User subject = new User()
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'name.familyName', 'Qiu')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'familyName', multiValued: false, clazz: String),
                                new Schema.Attribute(name: 'givenName', multiValued: false, clazz: String)
                        ],
                        clazz: User.Name
                )
        ])

        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider())[0]
        pathRef.modify(context, Configuration.withResourceObjectProvider())

        Assert.assertEquals('Qiu', subject.name.familyName)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider())[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Qiu', subject.get('name').get('lastName'))
    }

    @Test
    void testAddSimpleAttributeWithResourceObjectProvider() {
        User subject = new User()
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'userName', 'davidiamyou')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'userName',
                        multiValued: false,
                        clazz: String
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider())[0]
        pathRef.modify(context, Configuration.withResourceObjectProvider())

        Assert.assertEquals('davidiamyou', subject.userName)
    }

    @Test
    void testRemoveSimpleAttribute() {
        Map subject = ['name': ['firstName': 'David']]
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.REMOVE, 'name.firstName', null)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider())[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertNull(subject.get('name').get('firstName'))
    }

    @Test
    void testRemoveSimpleAttributeWithResourceObjectProvider() {
        User subject = new User(name: new User.Name(familyName: 'Qiu', givenName: 'Weinan'))
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.REMOVE, 'name.familyName', null)
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'familyName', multiValued: false, clazz: String),
                                new Schema.Attribute(name: 'givenName', multiValued: false, clazz: String)
                        ],
                        clazz: String
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider())[0]
        pathRef.modify(context, Configuration.withResourceObjectProvider())

        Assert.assertNull(subject.name.familyName)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider())[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals('Weinan', subject.get('name').get('firstName'))
    }

    @Test
    void testReplaceSimpleAttributeWithResourceObjectProvider() {
        User subject = new User(name: new User.Name(familyName: 'Qiu', givenName: 'Weinan'))
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.REPLACE, 'name.givenName', 'David')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'name',
                        multiValued: false,
                        subAttributes: [
                                new Schema.Attribute(name: 'familyName', multiValued: false, clazz: String),
                                new Schema.Attribute(name: 'givenName', multiValued: false, clazz: String)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider())[0]
        pathRef.modify(context, Configuration.withResourceObjectProvider())

        Assert.assertEquals('David', subject.name.givenName)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider())[0]
        pathRef.modify(context, Configuration.withMapObjectProvider())

        Assert.assertEquals(2, subject.get('emails').size())
        Assert.assertEquals('foo@bar.com', subject.get('emails')[0].get('value'))
        Assert.assertEquals('bar@foo.com', subject.get('emails')[1].get('value'))
    }

    @Test
    void testAddArrayAttributeWithResourceObjectProvider() {
        User subject = new User(
                emails: [
                        new User.Email(value: 'foo@bar.com', primary: true)
                ]
        )
        ModificationUnit unit = new ModificationUnit(ModificationUnit.Operation.ADD, 'emails', new User.Email(value: 'bar@foo.com', primary: false))
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathRef pathRef = PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider())[0]
        pathRef.modify(context, Configuration.withResourceObjectProvider())

        Assert.assertEquals(2, subject.emails.size())
        Assert.assertEquals('foo@bar.com', subject.emails[0].value)
        Assert.assertEquals('bar@foo.com', subject.emails[1].value)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider()).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('A', subject.get('emails')[0].get('status'))
        Assert.assertEquals('B', subject.get('emails')[1].get('status'))
        Assert.assertEquals('B', subject.get('emails')[2].get('status'))
    }

    @Test
    void testAddSimpleAttributeWithFilterWithResourceObjectProvider() {
        User subject = new User(emails: [
                new User.Email(value: 'foo@bar.com', primary: true, display: 'A'),
                new User.Email(value: 'bar@foo.com', primary: false, display: 'A'),
                new User.Email(value: 'foobar@foo.com', primary: false, display: 'A')
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.ADD,
                'emails[primary eq false].display', 'B')
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'display', multiValued: false)
                        ]
                )
        ])
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider()).each {
            it.modify(context, Configuration.withResourceObjectProvider())
        }

        Assert.assertEquals(3, subject.emails.size())
        Assert.assertEquals('A', subject.emails[0].display)
        Assert.assertEquals('B', subject.emails[1].display)
        Assert.assertEquals('B', subject.emails[2].display)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider()).each {
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
                                new Schema.Attribute(name: 'value', multiValued: false),
                                new Schema.Attribute(name: 'primary', multiValued: false),
                                new Schema.Attribute(name: 'status', multiValued: false)
                        ]
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REMOVE,
                'emails[primary eq false].status', null)
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider()).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('A', subject.get('emails')[1].get('status'))
        Assert.assertNull(subject.get('emails')[0].get('status'))
        Assert.assertNull(subject.get('emails')[2].get('status'))
    }

    @Test
    void testRemoveAttributeWithFilterWithResourceObjectProvider() {
        User subject = new User(emails: [
                new User.Email(value: 'foo@bar.com', primary: true, display: 'A'),
                new User.Email(value: 'bar@foo.com', primary: false, display: 'A'),
                new User.Email(value: 'foobar@foo.com', primary: false, display: 'A')
        ])
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'display', multiValued: false)
                        ]
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REMOVE,
                'emails[primary eq false].display', null)
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider()).each {
            it.modify(context, Configuration.withResourceObjectProvider())
        }

        Assert.assertEquals(3, subject.emails.size())
        Assert.assertEquals('A', subject.emails[0].display)
        Assert.assertNull(subject.emails[1].display)
        Assert.assertNull(subject.emails[2].display)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider()).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(3, subject.get('emails').size())
        Assert.assertEquals('david@foo.com', subject.get('emails')[0].get('value'))
        Assert.assertEquals('bar@foo.com', subject.get('emails')[1].get('value'))
        Assert.assertEquals('foobar@foo.com', subject.get('emails')[2].get('value'))
    }

    @Test
    void testReplaceArrayElementAttributeWithFilterWithResourceObjectProvider() {
        User subject = new User(emails: [
                new User.Email(value: 'foo@bar.com', primary: true),
                new User.Email(value: 'bar@foo.com', primary: true)
        ])
        Schema subjectSchema = new Schema(attributes: [
                new Schema.Attribute(
                        name: 'emails',
                        multiValued: true,
                        subAttributes: [
                                new Schema.Attribute(name: 'value', multiValued: false)
                        ]
                )
        ])
        ModificationUnit unit = new ModificationUnit(
                ModificationUnit.Operation.REPLACE,
                'emails[value eq "foo@bar.com"]', new User.Email(value: 'david@bar.com', primary: false))
        ModificationContext context = new ModificationContext(unit, subjectSchema, subject)
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider()).each {
            it.modify(context, Configuration.withResourceObjectProvider())
        }

        Assert.assertEquals(2, subject.emails.size())
        Assert.assertEquals('david@bar.com', subject.emails[0].value)
        Assert.assertEquals('bar@foo.com', subject.emails[1].value)
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withMapObjectProvider()).each {
            it.modify(context, Configuration.withMapObjectProvider())
        }

        Assert.assertEquals(1, subject.get('emails').size())
        Assert.assertEquals('bar@foo.com', subject.get('emails')[0].get('value'))
    }

    @Test
    void testRemoveArrayElementAttributeWithFilterWithResourceObjectProvider() {
        User subject = new User(emails: [
                new User.Email(value: 'foo@bar.com', primary: false),
                new User.Email(value: 'bar@foo.com', primary: false)
        ])
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
        CompilationContext compilationContext = CompilationContext.create(unit.getPath(), subject)
        PathCompiler.compile(compilationContext, Configuration.withResourceObjectProvider()).each {
            it.modify(context, Configuration.withResourceObjectProvider())
        }

        Assert.assertTrue(CollectionUtils.isEmpty(subject.emails))
    }
}
