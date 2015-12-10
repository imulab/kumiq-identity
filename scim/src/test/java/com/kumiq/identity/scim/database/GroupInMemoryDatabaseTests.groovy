package com.kumiq.identity.scim.database

import com.kumiq.identity.scim.resource.group.Group
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static org.hamcrest.Matchers.hasSize

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class GroupInMemoryDatabaseTests {

    InMemoryDatabase.GroupInMemoryDatabase database = new InMemoryDatabase.GroupInMemoryDatabase()

    @Before
    void setup() {
        Group group1 = new Group(id: 'group1', members: [
                new Group.Member(value: 'user1'),
                new Group.Member(value: 'group2'),
                new Group.Member(value: 'group3')
        ])
        Group group2 = new Group(id: 'group2', members: [
                new Group.Member(value: 'user2'),
                new Group.Member(value: 'group4')
        ])
        Group group3 = new Group(id: 'group3', members: [
                new Group.Member(value: 'user3')
        ])
        Group group4 = new Group(id: 'group4', members: [
                new Group.Member(value: 'user4')
        ])
        database.save(group1)
        database.save(group2)
        database.save(group3)
        database.save(group4)
    }

    @After
    void cleanUp() {
        database.reset()
    }

    @Test
    void testFindGroupsWithMember() {
        List<Group> nonTransitiveResults = database.findGroupsWithMember('user4', false)
        Assert.assertThat(nonTransitiveResults, hasSize(1))

        List<Group> transitiveResults = database.findGroupsWithMember('user4', true)
        Assert.assertThat(transitiveResults, hasSize(3))
    }

    @Test
    void testRemoveGroupMember() {
        database.deleteMember('user1')
        List<Group> transitiveResults = database.findGroupsWithMember('user1', true)
        Assert.assertThat(transitiveResults, hasSize(0))
    }
}
