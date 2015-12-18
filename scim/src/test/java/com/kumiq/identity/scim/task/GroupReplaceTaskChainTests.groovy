package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.config.TaskConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.group.Group
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
class GroupReplaceTaskChainTests {

    @Autowired
    ResourceDatabase.GroupDatabase groupDatabase

    @Resource(name = 'groupReplaceTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Date creationTime = new Date()
        Group group1 = new Group(
                id: '02B52AB8-ACFF-4041-BC60-F5249D12F905',
                displayName: 'group1',
                meta: new Meta(
                        created: creationTime, lastModified: creationTime, version: 1l))
        Group group2 = new Group(
                id: '44186815-3E97-4D76-9426-B37FBB1B2563',
                displayName: 'group2',
                meta: new Meta(
                        created: creationTime, lastModified: creationTime, version: 1l))
        groupDatabase.save(group1);
        groupDatabase.save(group2);
    }

    @After
    void cleanUp() {
        (groupDatabase as InMemoryDatabase.GroupInMemoryDatabase).reset()
    }

    @Test
    void testReplaceNormalGroup() {
        Group group = new Group(
                displayName: 'group3'
        )
        ReplaceContext context = new UserReplaceContext(resource: group, id: '02B52AB8-ACFF-4041-BC60-F5249D12F905')
        taskChain.perform(context)

        Assert.assertEquals('02B52AB8-ACFF-4041-BC60-F5249D12F905', group.id)
        Assert.assertEquals('group3', group.displayName)
        Assert.assertEquals(2l, group.meta?.version)
        Assert.assertNotNull(group.meta?.created)
        Assert.assertNotNull(group.meta?.lastModified)
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration, TaskConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml'])
    static class TestConfiguration {

    }
}
