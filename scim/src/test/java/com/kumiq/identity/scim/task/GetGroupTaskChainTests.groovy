package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.user.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ImportResource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import javax.annotation.Resource

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = [TestConfiguration])
@ActiveProfiles('test')
class GetGroupTaskChainTests {

    @Autowired
    ResourceDatabase.GroupDatabase groupDatabase

    @Resource(name = 'getGroupTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Group group = new Group(id: 'testGroup', displayName: 'testGroup', members: [
                new Group.Member(value: 'User1', display: 'User1'),
                new Group.Member(value: 'User2', display: 'User2'),
                new Group.Member(value: 'User3', display: 'User3')
        ])
        groupDatabase.save(group)
    }

    @After
    void cleanUp() {
        groupDatabase.delete('testGroup')
    }

    @Test
    void testGetGroup() {
        GroupGetContext<Group> context = new GroupGetContext<>(id: 'testGroup')
        taskChain.perform(context)

        Assert.assertNotNull(context.resource)
        Assert.assertNotNull(context.data)
        Assert.assertEquals('testGroup', context.data.get('id'))
        Assert.assertEquals('testGroup', context.data.get('displayName'))
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/GroupGetTaskConfig.xml'])
    static class TestConfiguration {

    }
}
