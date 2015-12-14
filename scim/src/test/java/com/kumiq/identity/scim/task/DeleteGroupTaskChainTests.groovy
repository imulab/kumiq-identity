package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.user.User
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
class DeleteGroupTaskChainTests {

    @Autowired
    ResourceDatabase.GroupDatabase groupDatabase

    @Resource(name = 'deleteGroupTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Group group = new Group(id: 'testGroup', displayName: 'testGroup')
        groupDatabase.save(group);
    }

    @Test
    void testDeleteGroup() {
        GroupDeleteContext context = new GroupDeleteContext(id: 'testGroup')
        taskChain.perform(context)

        Assert.assertFalse(groupDatabase.findById('testGroup').isPresent())
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/GroupDeleteTaskConfig.xml'])
    static class TestConfiguration {

    }
}
