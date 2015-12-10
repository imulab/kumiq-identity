package com.kumiq.identity.scim.spi

import com.kumiq.identity.scim.resource.user.User
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
class ResourceObjectProviderTests {

    ObjectProvider provider
    User resource

    @Before
    void setup() {
        provider = new ResourceObjectProvider()
        resource = new User(
                userName: 'davidiamyou',
                emails: [
                    new User.Email(value: 'foo@bar.com'),
                    new User.Email(value: 'bar@bar.com')
                ]
        )
    }

    @Test
    void testGetArrayIndex() {
        Assert.assertEquals('foo@bar.com', (provider.getArrayIndex(resource.emails, 0) as User.Email).value)
        Assert.assertEquals('bar@bar.com', (provider.getArrayIndex(resource.emails, 1) as User.Email).value)
    }

    @Test
    void testAddToArray() {
        provider.addToArray(resource.emails, [new User.Email(value: 'foobar@bar.com')])
        Assert.assertEquals('foobar@bar.com', (provider.getArrayIndex(resource.emails, 2) as User.Email).value)
    }

    @Test
    void testGetPropertyValue() {
        Assert.assertEquals('davidiamyou', provider.getPropertyValue(resource, 'userName'))
        Assert.assertEquals('foo@bar.com', provider.getPropertyValue(resource.emails[0], 'value'))
    }

    @Test
    void testSetPropertyValue() {
        provider.setPropertyValue(resource, 'userName', 'imu')
        provider.setPropertyValue(resource.emails[0], 'value', 'foo@foo.com')
        Assert.assertEquals('imu', provider.getPropertyValue(resource, 'userName'))
        Assert.assertEquals('foo@foo.com', provider.getPropertyValue(resource.emails[0], 'value'))
    }

    @Test
    void testRemovePropertyValue() {
        provider.removePropertyValue(resource.emails[0], 'value')
        Assert.assertNull(provider.getPropertyValue(resource.emails[0], 'value'))
    }
}
