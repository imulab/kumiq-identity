package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.PostModificationNullArrayProtectionTask
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
class PostModificationNullArrayProtectionTaskTests {

    PostModificationNullArrayProtectionTask task
    Schema testSchema

    @Before
    void setup() {
        task = new PostModificationNullArrayProtectionTask()
        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'emails', multiValued: true),
                new Schema.Attribute(name: 'phoneNumbers', multiValued: true)
        ])
    }

    @Test
    void testProtectNullEmails() {
        User user = new User(emails: null, phoneNumbers: [])
        ReplaceContext<User> context = new UserReplaceContext(resource: user)
        context.setSchema(testSchema)

        task.perform(context)

        Assert.assertNotNull(context.resource.emails)
    }
}
