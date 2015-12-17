package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.DefaultReadOnlyTask
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
class DefaultReadOnlyTaskTests {

    @Test
    void testDefaultReadOnlyAttributes() {
        DefaultReadOnlyTask task = new DefaultReadOnlyTask()
        Schema schema = new Schema(attributes: [
                new Schema.Attribute(name: 'id', mutability: ScimConstants.MUTABILITY_READONLY),
                new Schema.Attribute(name: 'meta', mutability: ScimConstants.MUTABILITY_READONLY, subAttributes: [
                        new Schema.Attribute(name: 'version', mutability: ScimConstants.MUTABILITY_READONLY)
                ]),
                new Schema.Attribute(name: 'name', mutability: ScimConstants.MUTABILITY_READWRITE, subAttributes: [
                        new Schema.Attribute(name: 'familyName', mutability: ScimConstants.MUTABILITY_READONLY)
                ]),
                new Schema.Attribute(name: 'userName', mutability: ScimConstants.MUTABILITY_READWRITE)
        ])
        User original = new User(id: 'user1', meta: new Meta(version: 3l), userName: 'david', name: new User.Name(familyName: 'Qiu'))
        User resource = new User(id: 'user2', meta: null, userName: 'mike', name: null)
        ReplaceContext context = new UserReplaceContext(originalCopy: original, resource: resource, schema: schema)

        task.perform(context)

        Assert.assertEquals('user1', resource.id)
        Assert.assertEquals(3l, resource.meta.version)
        Assert.assertEquals('mike', resource.userName)
        Assert.assertNull(resource.name?.familyName)
    }
}
