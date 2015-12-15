package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.core.Meta
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
class UserQueryTaskChainTests {

    @Autowired
    ResourceDatabase.UserDatabase userDatabase

    @Resource(name = 'userQueryTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Date creationTime = new Date()
        User user1 = new User(id: '02B52AB8-ACFF-4041-BC60-F5249D12F905', userName: 'user1', active: true, meta: new Meta(created: creationTime))
        User user2 = new User(id: '5FA9C865-B78E-43C5-95C2-3BE182EDCA71', userName: 'user2', active: false, meta: new Meta(created: creationTime.plus(1)))
        User user3 = new User(id: '9AB0FD99-D366-45E2-8890-C79C52FD10CC', userName: 'user3', active: true, meta: new Meta(created: creationTime.plus(2)))
        User user4 = new User(id: '8D66F01F-BA9D-447C-B3CF-4997ED5DFBCF', userName: 'user4', active: true, meta: new Meta(created: creationTime.minus(1)))
        User user5 = new User(id: '8B1FE4C0-F389-422F-B1C1-BBDFBD5B41C3', userName: 'user5', active: false, meta: new Meta(created: creationTime.plus(3)))
        User user6 = new User(id: '566F9DEE-CBEC-40B0-8EE2-47A0947D7F19', userName: 'user6', active: true, meta: new Meta(created: creationTime.minus(3)))
        userDatabase.save(user1);
        userDatabase.save(user2);
        userDatabase.save(user3);
        userDatabase.save(user4);
        userDatabase.save(user5);
        userDatabase.save(user6);
    }

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
    }

    @Test
    void testQueryActiveUser() {
        UserQueryContext context = new UserQueryContext(attributes: ['userName'], filter: 'active eq true', count: 20)
        taskChain.perform(context)

        Assert.assertEquals(4, context.results.get(UserQueryContext.TOTAL_RESULTS))
        Assert.assertEquals(20, context.results.get(UserQueryContext.PAGE_COUNT))
        Assert.assertEquals(1, context.results.get(UserQueryContext.START_INDEX))

        List resourceList = context.results.get(UserQueryContext.RESOURCES)
        Assert.assertEquals(4, resourceList.size())
        Assert.assertEquals('user3', (resourceList[0] as Map).get('userName'))
        Assert.assertEquals('user1', (resourceList[1] as Map).get('userName'))
        Assert.assertEquals('user4', (resourceList[2] as Map).get('userName'))
        Assert.assertEquals('user6', (resourceList[3] as Map).get('userName'))
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/UserQueryTaskConfig.xml', 'config/UserGetTaskConfig.xml'])
    static class TestConfiguration {

    }
}
