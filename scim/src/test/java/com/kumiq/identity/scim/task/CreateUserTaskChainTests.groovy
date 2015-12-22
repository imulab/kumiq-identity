package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.config.DefaultDatabaseConfiguration
import com.kumiq.identity.scim.database.InMemoryDatabase
import com.kumiq.identity.scim.database.ResourceDatabase.GroupDatabase
import com.kumiq.identity.scim.database.ResourceDatabase.UserDatabase
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.user.User
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
class CreateUserTaskChainTests {

    @Resource(name = 'createUserTaskChain')
    SimpleTaskChain task

    @Autowired
    UserDatabase userDatabase

    @Autowired
    GroupDatabase groupDatabase

    @After
    void cleanUp() {
        (userDatabase as InMemoryDatabase.UserInMemoryDatabase).reset()
        (groupDatabase as InMemoryDatabase.GroupInMemoryDatabase).reset()
    }

    @Test
    void testCreateCorrectUser() {
        User user = new User(userName: 'newUser')
        UserCreateContext context = new UserCreateContext(resource: user)
        task.perform(context)

        Assert.assertTrue(userDatabase.query('userName eq "newUser"', 'meta.created', true)?.size() == 1)
    }

    @Test(expected = ExceptionFactory.ResourceUniquenessViolatedException)
    void testCreateDuplicateUser() {
        User user1 = new User(id: 'F8019997-2342-49AE-9066-946949D64790', userName: 'newUser')
        userDatabase.save(user1)

        User user = new User(userName: 'newUser')
        UserCreateContext context = new UserCreateContext(resource: user)
        task.perform(context)
    }

    @Test(expected = ExceptionFactory.ResourceAttributeAbsentException)
    void testCreateUserWithMissingAttribute() {
        User user = new User(displayName: 'User1')
        UserCreateContext context = new UserCreateContext(resource: user)
        task.perform(context)
    }

    @Test(expected = ExceptionFactory.ResourceReferenceViolatedException)
    void testCreateUserWithMissingReference() {
        User user = new User(userName: 'newUser', groups: [
                new User.Group(value: 'unknownGroup')
        ])
        UserCreateContext context = new UserCreateContext(resource: user)
        task.perform(context)
    }

    @Test
    void testCreateUserWithGroup() {
        Group group = new Group(id: '7BD66683-F4DC-476C-8F8C-D03FC5A262A1', displayName: 'TestGroup')
        groupDatabase.save(group)

        User user = new User(userName: 'newUser', displayName: 'newUser', groups: [
                new User.Group(value: '7BD66683-F4DC-476C-8F8C-D03FC5A262A1')
        ])
        UserCreateContext context = new UserCreateContext(resource: user)
        task.perform(context)

        Assert.assertTrue((groupDatabase.findById('7BD66683-F4DC-476C-8F8C-D03FC5A262A1').get() as Group)
                .members.collect { it.display }.contains('newUser'))
    }

    @Configuration
    @EnableAutoConfiguration
    @Import([DefaultDatabaseConfiguration])
    @ImportResource(['config/DataInitializationConfig.xml', 'config/UserGetTaskConfig.xml', 'config/UserCreateTaskConfig.xml'])
    static class TestConfiguration {

    }
}
