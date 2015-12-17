package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.IgnoreReadOnlyTask
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
class IgnoreReadOnlyTaskTests {

    @Test
    void testIgnoreReadOnlyId() {
        Schema schema = new Schema(attributes: [
                new Schema.Attribute(name: 'id', mutability: ScimConstants.MUTABILITY_READONLY),
                new Schema.Attribute(name: 'userName', mutability: ScimConstants.MUTABILITY_READWRITE)
        ])
        User user = new User(id: '4917C86C-04EE-4F78-AF62-8FFF247A29DA', userName: 'david')
        UserCreateContext context = new UserCreateContext(resource: user, schema: schema)
        IgnoreReadOnlyTask task = new IgnoreReadOnlyTask()
        task.perform(context)

        Assert.assertNull(user.id)
        Assert.assertEquals('david', user.userName)
    }
}
