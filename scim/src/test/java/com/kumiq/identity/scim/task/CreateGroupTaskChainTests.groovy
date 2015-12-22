package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.exception.ExceptionFactory
import org.junit.After
import org.junit.Assert
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
class CreateGroupTaskChainTests {

    @Resource(name = 'createGroupTaskChain')
    SimpleTaskChain task

    @Autowired
    ResourceDatabase.UserDatabase userDatabase

    @Autowired
    ResourceDatabase.GroupDatabase groupDatabase

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
        (groupDatabase as InMemoryDatabase.GroupInMemoryDatabase).reset()
    }

    @Test
    void testCreateCorrectGroup() {
        Group group = new Group(displayName: 'newGroup')
        GroupCreateContext context = new GroupCreateContext(resource: group)
        task.perform(context)

        Assert.assertTrue(groupDatabase.query('displayName eq "newGroup"', 'meta.created', true)?.size() == 1)
    }

    @Test(expected = ExceptionFactory.ResourceReferenceViolatedException)
    void testCreateGroupWithMissingReference() {
        Group group = new Group(displayName: 'newGroup', members: [
                new Group.Member(value: 'someGroup'),
                new Group.Member(value: 'someUser')
        ])
        GroupCreateContext context = new GroupCreateContext(resource: group)
        task.perform(context)
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource([
            'config/DataInitializationConfig.xml',
            'config/UserGetTaskConfig.xml',
            'config/UserCreateTaskConfig.xml',
            'config/GroupGetTaskConfig.xml',
            'config/GroupCreateTaskConfig.xml'])
    static class TestConfiguration {

    }
}
