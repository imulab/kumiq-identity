package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.user.create.CheckUserUniquenessTask
import com.kumiq.identity.scim.exception.ExceptionFactory.ResourceUniquenessViolatedException
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
class CheckUniquenessTaskTests {

    CheckUserUniquenessTask task
    ResourceDatabase.UserDatabase database = new InMemoryDatabase.UserInMemoryDatabase()
    Schema testSchema

    @Before
    void setup() {
        task = new CheckUserUniquenessTask()
        database = new InMemoryDatabase.UserInMemoryDatabase()
        task.setUserDatabase(database)

        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'id', uniqueness: ScimConstants.UNIQUENESS_SERVER),
                new Schema.Attribute(name: 'userName', uniqueness: ScimConstants.UNIQUENESS_SERVER),
                new Schema.Attribute(name: 'name', uniqueness: ScimConstants.UNIQUENESS_NONE, subAttributes: [
                        new Schema.Attribute(name: 'givenName', uniqueness:  ScimConstants.UNIQUENESS_NONE),
                        new Schema.Attribute(name: 'familyName', uniqueness:  ScimConstants.UNIQUENESS_NONE)
                ])
        ])

        User user1 = new User(id: 'testUser1', userName: 'testUser1', name: new User.Name(givenName: 'user1', familyName: 'test'))
        User user2 = new User(id: 'testUser2', userName: 'testUser2', name: new User.Name(givenName: 'user2', familyName: 'test'))
        database.save(user1)
        database.save(user2)
    }

    @After
    void cleanUp() {
        (database as InMemoryDatabase.UserInMemoryDatabase).reset()
    }

    @Test
    void testViolateUniqueness() {
        User user = new User(id: 'testUser3', userName: 'testUser1', name: new User.Name(givenName: 'user1', familyName: 'test'))
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)

        try {
            task.perform(context)
            Assert.fail('test should have failed')
        } catch (ResourceUniquenessViolatedException ex) {
            Assert.assertEquals('testUser3', ex.resourceId)
            Assert.assertEquals('userName', ex.path)
        }
    }

    @Test
    void testConformUniqueness() {
        User user = new User(id: 'testUser4', userName: 'testUser4', name: new User.Name(givenName: 'user4', familyName: 'test'))
        UserCreateContext context = new UserCreateContext(resource: user, schema: testSchema)
        task.perform(context)
    }
}
