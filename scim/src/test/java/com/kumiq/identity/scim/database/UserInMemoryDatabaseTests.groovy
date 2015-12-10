package com.kumiq.identity.scim.database

import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.user.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class UserInMemoryDatabaseTests {

    InMemoryDatabase.UserInMemoryDatabase userDatabase = new InMemoryDatabase.UserInMemoryDatabase();

    @Before
    void setup() {
        User user1 = new User(
                id: '60001E54-C36D-4518-8235-1CBF9840FE1D',
                meta: new Meta(version: 1),
                userName: 'user1',
                emails: [
                        new User.Email(value: 'user1@foo.com', primary: true),
                        new User.Email(value: 'user1@bar.com', primary: false)
                ]
        )
        User user2 = new User(
                id: '576DD5BF-DD2D-4E5F-9C4A-3DB42FF93AA0',
                meta: new Meta(version: 2),
                userName: 'user2',
                emails: [
                        new User.Email(value: 'user2@foo.com', primary: true),
                        new User.Email(value: 'user2@bar.com', primary: false)
                ]
        )
        User user3 = new User(
                id: '39FDAB85-C40E-4E56-975E-E9B767F3A4D0',
                meta: new Meta(version: 3),
                userName: 'user3',
                emails: [
                        new User.Email(value: 'user3@foo.com', primary: true),
                        new User.Email(value: 'user3@bar.com', primary: false)
                ]
        )
        userDatabase.save(user1)
        userDatabase.save(user2)
        userDatabase.save(user3)
    }

    @After
    void cleanUp() {
        userDatabase.reset();
    }

    @Test
    void testQuery() {
        List<User> results = userDatabase.query('meta.version ge 2', null, false)

        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.collect {it.userName}.contains('user2'))
        Assert.assertTrue(results.collect {it.userName}.contains('user3'))
    }

    @Test
    void testQueryWithSort() {
        List<User> results = userDatabase.query('meta.version ge 2', 'meta.version', true)

        Assert.assertEquals(2, results.size());
        Assert.assertEquals('user2', results[0].userName)
        Assert.assertEquals('user3', results[1].userName)
    }
}
