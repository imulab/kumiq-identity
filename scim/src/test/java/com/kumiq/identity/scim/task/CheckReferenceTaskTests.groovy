package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.CheckReferenceTask
import com.kumiq.identity.scim.task.user.create.CheckUserReferenceTask
import com.kumiq.identity.scim.exception.ExceptionFactory.ResourceReferenceViolatedException
import org.junit.After
import org.junit.Assert
import org.junit.Before
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
class CheckReferenceTaskTests {

    CheckReferenceTask task
    ResourceDatabase.UserDatabase userDatabase
    ResourceDatabase.GroupDatabase groupDatabase
    Schema testSchema

    @Before
    void setup() {
        task = new CheckUserReferenceTask()
        userDatabase = new InMemoryDatabase.UserInMemoryDatabase()
        groupDatabase = new InMemoryDatabase.GroupInMemoryDatabase()
        task.userDatabase = userDatabase
        task.groupDatabase = groupDatabase

        testSchema = new Schema(
                attributes: [
                        new Schema.Attribute(name: 'groups', multiValued: true, subAttributes: [
                                new Schema.Attribute(name: 'value', referenceTypes: [ScimConstants.REF_TYPE_GROUP])
                        ])
                ]
        )

        Group group1 = new Group(id: 'testGroup1', displayName: 'testGroup1')
        Group group2 = new Group(id: 'testGroup2', displayName: 'testGroup2')
        groupDatabase.save(group1)
        groupDatabase.save(group2)
    }

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
        (groupDatabase as InMemoryDatabase.GroupInMemoryDatabase).reset()
    }

    @Test
    void testViolateReference() {
        User user = new User(groups: [
                new User.Group(value: 'testGroup1'),
                new User.Group(value: 'unknownGroup')
        ])
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)

        try {
            task.perform(context)
            Assert.fail('task should have failed.')
        } catch (ResourceReferenceViolatedException ex) {
            Assert.assertEquals('groups.value', ex.path)
        }
    }

    @Test
    void testConformToConstraint() {
        User user = new User(groups: [
                new User.Group(value: 'testGroup1'),
                new User.Group(value: 'testGroup2')
        ])
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)
        task.perform(context)
    }
}
