package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.CheckRequiredTask
import com.kumiq.identity.scim.task.user.create.CheckUserRequiredTask
import com.kumiq.identity.scim.exception.ExceptionFactory.ResourceAttributeAbsentException
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
    Schema testSchema

    @Before
    void setup() {
        task = new CheckUserRequiredTask()

        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'id', required: true),
                new Schema.Attribute(name: 'userName', required: true),
                new Schema.Attribute(name: 'name', required: false, subAttributes: [
                        new Schema.Attribute(name: 'givenName', required: false),
                        new Schema.Attribute(name: 'familyName', required: false)
                ])
        ])
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
