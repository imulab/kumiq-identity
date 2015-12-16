package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.user.create.CheckRequiredTask
import com.kumiq.identity.scim.utils.ExceptionFactory.ResourceAttributeAbsentException
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
class CheckRequiredTaskTests {

    CheckRequiredTask task
    ResourceDatabase.UserDatabase userDatabase
    Schema testSchema

    @Before
    void setup() {
        task = new CheckRequiredTask()
        userDatabase = new InMemoryDatabase.UserInMemoryDatabase()
        task.setUserDatabase(userDatabase)

        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'id', required: true),
                new Schema.Attribute(name: 'userName', required: true),
                new Schema.Attribute(name: 'name', required: false, subAttributes: [
                        new Schema.Attribute(name: 'givenName', required: false),
                        new Schema.Attribute(name: 'familyName', required: false)
                ])
        ])

        User user1 = new User(id: 'testUser1', userName: 'testUser1', name: new User.Name(givenName: 'user1', familyName: 'test'))
        User user2 = new User(id: 'testUser2', userName: 'testUser2', name: new User.Name(givenName: 'user2', familyName: 'test'))
        userDatabase.save(user1)
        userDatabase.save(user2)
    }

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
    }

    @Test
    void testViolateRequired() {
        User user = new User(id: 'testUser3')
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)

        try {
            task.perform(context)
            Assert.fail('test should have failed.')
        } catch (ResourceAttributeAbsentException ex) {
            Assert.assertEquals('userName', ex.requiredPath)
        }
    }

    @Test
    void testConformConstraint() {
        User user = new User(id: 'testUser3', userName: 'david')
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)
        task.perform(context)
    }
}
