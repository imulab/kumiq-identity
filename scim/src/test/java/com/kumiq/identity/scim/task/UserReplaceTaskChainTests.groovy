package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.config.TaskConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.exception.ExceptionFactory
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
class UserReplaceTaskChainTests {

    @Autowired
    ResourceDatabase.UserDatabase userDatabase

    @Resource(name = 'userReplaceTaskChain')
    SimpleTaskChain taskChain

    @Before
    void setup() {
        Date creationTime = new Date()
        User user1 = new User(
                id: '02B52AB8-ACFF-4041-BC60-F5249D12F905',
                userName: 'user1',
                nickName: 'foo',
                active: true,
                meta: new Meta(
                        created: creationTime, lastModified: creationTime, version: 1l))
        User user2 = new User(
                id: '44186815-3E97-4D76-9426-B37FBB1B2563',
                userName: 'user2',
                nickName: 'foobar',
                active: true,
                meta: new Meta(
                        created: creationTime, lastModified: creationTime, version: 1l))
        userDatabase.save(user1);
        userDatabase.save(user2);
    }

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
    }

    @Test
    void testReplaceNormalUser() {
        User user = new User(
                userName: 'user3',
                nickName: 'bar',
                active: false
        )
        ReplaceContext context = new UserReplaceContext(resource: user, id: '02B52AB8-ACFF-4041-BC60-F5249D12F905')
        taskChain.perform(context)

        Assert.assertEquals('02B52AB8-ACFF-4041-BC60-F5249D12F905', user.id)
        Assert.assertEquals('user3', user.userName)
        Assert.assertEquals('bar', user.nickName)
        Assert.assertEquals(2l, user.meta?.version)
        Assert.assertNotNull(user.meta?.created)
        Assert.assertNotNull(user.meta?.lastModified)
    }

    @Test(expected = ExceptionFactory.ResourceUniquenessViolatedException)
    void testReplaceUserWithConflictUsername() {
        User user = new User(
                userName: 'user2',
                nickName: 'bar',
                active: false
        )
        ReplaceContext context = new UserReplaceContext(resource: user, id: '02B52AB8-ACFF-4041-BC60-F5249D12F905')
        taskChain.perform(context)
    }

    @Test(expected = ExceptionFactory.ResourceAttributeAbsentException)
    void testReplaceUserWithMissingUsername() {
        User user = new User(
                nickName: 'bar',
                active: false
        )
        ReplaceContext context = new UserReplaceContext(resource: user, id: '02B52AB8-ACFF-4041-BC60-F5249D12F905')
        taskChain.perform(context)
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration, TaskConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml'])
    static class TestConfiguration {

    }
}
