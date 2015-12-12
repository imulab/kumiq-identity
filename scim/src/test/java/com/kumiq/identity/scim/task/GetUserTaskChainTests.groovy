package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.ResourceDatabase.UserDatabase
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
class GetUserTaskChainTests {

    @Autowired
    UserDatabase userDatabase

    @Resource(name = 'getUserTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        User user = new User(id: 'testUser', userName: 'FooBar')
        userDatabase.save(user);
    }

    @After
    void cleanUp() {
        userDatabase.delete('testUser')
    }

    @Test
    void testGetUser() {
        UserGetContext<User> context = new UserGetContext<>(id: 'testUser')
        taskChain.perform(context)

        Assert.assertNotNull(context.resource)
        Assert.assertNotNull(context.data)
        Assert.assertEquals('testUser', context.data.get('id'))
        Assert.assertEquals('FooBar', context.data.get('userName'))
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/UserGetTaskConfig.xml'])
    static class TestConfiguration {

    }
}
