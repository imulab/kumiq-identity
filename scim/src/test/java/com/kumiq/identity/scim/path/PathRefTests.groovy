package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.resource.misc.Schema
import org.junit.Assert
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
class PathRefTests {

    @Test
    void testGetAttribute() {
        Schema userSchema = new Schema(
                attributes: [
                        new Schema.Attribute(
                                name: 'first',
                                subAttributes: [
                                        new Schema.Attribute(
                                                name: 'second',
                                                subAttributes: [
                                                        new Schema.Attribute(
                                                                name: 'third'
                                                        )
                                                ]
                                        )
                                ]
                        )
                ]
        )
        Schema.Attribute attribute = null
        PathRef root = PathCompiler.compile('first.second.third.forth', null).get(0)

        attribute = root.next.getAttribute(userSchema)
        Assert.assertEquals('first', attribute.name)

        attribute = root.next.next.getAttribute(userSchema)
        Assert.assertEquals('second', attribute.name)

        attribute = root.next.next.next.getAttribute(userSchema)
        Assert.assertEquals('third', attribute.name)

        attribute = root.next.next.next.next.getAttribute(userSchema)
        Assert.assertNull(attribute)
    }
}
