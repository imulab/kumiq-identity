package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.SyncGroupTask
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
class SyncGroupTaskTests {

    SyncGroupTask task
    ResourceDatabase.GroupDatabase groupDatabase

    @Before
    void setup() {
        groupDatabase = new InMemoryDatabase.GroupInMemoryDatabase()
        task = new SyncGroupTask()
        task.groupDatabase = groupDatabase
        Group g1 = new Group(id: 'g1', meta: new Meta(location: 'loc1'), displayName: 'g1', members: [
                new Group.Member(value: 'g2')
        ])
        Group g2 = new Group(id: 'g2', meta: new Meta(location: 'loc2'), displayName: 'g2', members: [
                new Group.Member(value: 'u1')
        ])
        groupDatabase.save(g1)
        groupDatabase.save(g2)

    }

    @Test
    void testSyncGroup() {
        User user = new User(id: 'u1', groups: [new User.Group(value: 'g2')])
        UserGetContext<User> context = new UserGetContext(resource: user)
        task.perform(context)

        Assert.assertTrue(context.resource.groups.collect {it.value}.contains('g1'))
        Assert.assertTrue(context.resource.groups.collect {it.value}.contains('g2'))
    }
}
