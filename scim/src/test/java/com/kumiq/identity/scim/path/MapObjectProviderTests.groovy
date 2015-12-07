package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.spi.MapObjectProvider
import com.kumiq.identity.scim.spi.ObjectProvider
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
class MapObjectProviderTests {

    ObjectProvider provider
    Map data

    @Before
    void setup() {
        provider = new MapObjectProvider()
        data = [
                'foo': 'foo',
                'bar': ['A', 'B', 'C'],
        ]
    }

    @Test
    void testGetPropertyKeys() {
        Collection<String> keys = provider.getPropertyKeys(data)
        Assert.assertTrue(keys.contains('foo'))
        Assert.assertTrue(keys.contains('bar'))
    }

    @Test
    void testGetArrayIndex() {
        Assert.assertEquals('A', provider.getArrayIndex(data.get('bar'), 0))
        Assert.assertEquals('B', provider.getArrayIndex(data.get('bar'), 1))
        Assert.assertEquals('C', provider.getArrayIndex(data.get('bar'), 2))
    }

    @Test
    void testAddToArray() {
        provider.addToArray(data.get('bar'), ['D', 'E'])
        Assert.assertEquals('D', provider.getArrayIndex(data.get('bar'), 3))
        Assert.assertEquals('E', provider.getArrayIndex(data.get('bar'), 4))
    }

    @Test
    void testGetPropertyValue() {
        Assert.assertEquals('foo', provider.getPropertyValue(data, 'foo'))
    }

    @Test
    void testSetPropertyValue() {
        provider.setPropertyValue(data, 'foo', 'bar')
        provider.setPropertyValue(data, 'bar', null)
        Assert.assertEquals('bar', provider.getPropertyValue(data, 'foo'))
        Assert.assertNull(provider.getPropertyValue(data, 'bar'))
    }

    @Test
    void testRemovePropertyValue() {
        provider.removePropertyValue(data, 'bar')
        Assert.assertNull(provider.getPropertyValue(data, 'bar'))
    }
}
