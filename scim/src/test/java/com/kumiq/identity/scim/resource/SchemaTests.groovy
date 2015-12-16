package com.kumiq.identity.scim.resource

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
class SchemaTests {

    @Test
    void testFindAllPaths() {
        Schema schema = new Schema(
                attributes: [
                        new Schema.Attribute(name: 'id'),
                        new Schema.Attribute(name: 'name', subAttributes: [
                                new Schema.Attribute(name: 'givenName'),
                                new Schema.Attribute(name: 'familyName'),
                                new Schema.Attribute(name: 'middleName'),
                        ]),
                        new Schema.Attribute(name: 'userName'),
                        new Schema.Attribute(name: 'emails', subAttributes: [
                                new Schema.Attribute(name: 'value'),
                                new Schema.Attribute(name: 'displayName'),
                                new Schema.Attribute(name: 'type'),
                        ]),
                ]
        )

        List<String> allPaths = schema.findAllPaths()
        Assert.assertTrue(allPaths.contains('id'))
        Assert.assertTrue(allPaths.contains('name'))
        Assert.assertTrue(allPaths.contains('name.givenName'))
        Assert.assertTrue(allPaths.contains('name.familyName'))
        Assert.assertTrue(allPaths.contains('name.middleName'))
        Assert.assertTrue(allPaths.contains('userName'))
        Assert.assertTrue(allPaths.contains('emails'))
        Assert.assertTrue(allPaths.contains('emails.value'))
        Assert.assertTrue(allPaths.contains('emails.displayName'))
        Assert.assertTrue(allPaths.contains('emails.type'))
    }
}
