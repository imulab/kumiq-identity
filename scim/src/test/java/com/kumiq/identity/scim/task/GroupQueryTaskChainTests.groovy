package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase.GroupDatabase
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
class GroupQueryTaskChainTests {

    @Autowired
    GroupDatabase groupDatabase

    @Resource(name = 'groupQueryTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Date creationTime = new Date()
        Group group1 = new Group(id: '02B52AB8-ACFF-4041-BC60-F5249D12F905', displayName: 'ABC', meta: new Meta(created: creationTime))
        Group group2 = new Group(id: '5FA9C865-B78E-43C5-95C2-3BE182EDCA71', displayName: 'ABD', meta: new Meta(created: creationTime.plus(1)))
        Group group3 = new Group(id: '9AB0FD99-D366-45E2-8890-C79C52FD10CC', displayName: 'BCD', meta: new Meta(created: creationTime.plus(2)))
        Group group4 = new Group(id: '8D66F01F-BA9D-447C-B3CF-4997ED5DFBCF', displayName: 'CDQ', meta: new Meta(created: creationTime.minus(1)))
        groupDatabase.save(group1)
        groupDatabase.save(group2)
        groupDatabase.save(group3)
        groupDatabase.save(group4)
    }

    @After
    void cleanUp() {
        (groupDatabase as InMemoryDatabase.GroupInMemoryDatabase).reset()
    }

    @Test
    void testQueryGroupName() {
        GroupQueryContext context = new GroupQueryContext(attributes: ['displayName'], filter: 'displayName ew "D"', count: 20)
        taskChain.perform(context)

        Assert.assertEquals(2, context.results.get(UserQueryContext.TOTAL_RESULTS))
        Assert.assertEquals(20, context.results.get(UserQueryContext.PAGE_COUNT))
        Assert.assertEquals(1, context.results.get(UserQueryContext.START_INDEX))

        List resourceList = context.results.get(UserQueryContext.RESOURCES)
        Assert.assertEquals(2, resourceList.size())
        Assert.assertEquals('BCD', (resourceList[0] as Map).get('displayName'))
        Assert.assertEquals('ABD', (resourceList[1] as Map).get('displayName'))
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/GroupQueryTaskConfig.xml', 'config/GroupGetTaskConfig.xml'])
    static class TestConfiguration {

    }
}
