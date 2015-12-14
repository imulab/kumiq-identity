package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.misc.Schema
import jdk.nashorn.internal.runtime.regexp.joni.Config
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import static com.kumiq.identity.scim.resource.constant.ScimConstants.REF_TYPE_SCIM
import static com.kumiq.identity.scim.resource.constant.ScimConstants.RETURNED_NEVER;
import static com.kumiq.identity.scim.resource.constant.ScimConstants.RETURNED_DEFAULT;

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RunWith(JUnit4)
class ResourceMapperTests {

    Map data
    Schema schema

    @Before
    void setup() {
        data = [
                'userName': 'davidiamyou',
                'name': ['firstName': 'Weinan', 'lastName': 'Qiu'],
                'emails': [
                        ['value': 'foo@bar.com', 'primary': true],
                        ['value': 'bar@bar.com', 'primary': false]
                ],
                'password': 's3cret',
                'enterpriseExtension': ['employeeNumber' : '1001']
        ]
        schema = new Schema(
                attributes: [
                        new Schema.Attribute(name: 'userName', returned: RETURNED_DEFAULT, multiValued: false),
                        new Schema.Attribute(name: 'name', returned: RETURNED_DEFAULT, multiValued: false, subAttributes: [
                                new Schema.Attribute(name: 'firstName', returned: RETURNED_DEFAULT, multiValued: false),
                                new Schema.Attribute(name: 'lastName', returned: RETURNED_DEFAULT, multiValued: false)
                        ]),
                        new Schema.Attribute(name: 'emails', returned: RETURNED_DEFAULT, multiValued: true, subAttributes: [
                                new Schema.Attribute(name: 'value', returned: RETURNED_DEFAULT, multiValued: false),
                                new Schema.Attribute(name: 'primary', returned: RETURNED_DEFAULT, multiValued: false)
                        ]),
                        new Schema.Attribute(name: 'password', returned: RETURNED_NEVER, multiValued: false),
                        new Schema.Attribute(name: ScimConstants.URN_ENTERPRISE_USER_EXTENSION, property: 'enterpriseExtension', returned: RETURNED_DEFAULT, multiValued: false, subAttributes: [
                                new Schema.Attribute(name: 'employeeNumber', returned: RETURNED_DEFAULT, multiValued: false)
                        ])
                ]
        )
    }

    @Test
    void testMapResourceToApiName() {
        MappingContext context = new MappingContext(data, schema, ['urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.employeeNumber'], [])
        Configuration configuration = Configuration.withMapObjectProvider().withOption(Configuration.Option.API_ATTR_NAME_PREF)
        Map result = ResourceMapper.convertToMap(context, configuration)

        Assert.assertTrue(result.containsKey('urn:ietf:params:scim:schemas:extension:enterprise:2.0:User'))
        Assert.assertTrue((result.get('urn:ietf:params:scim:schemas:extension:enterprise:2.0:User') as Map).containsKey('employeeNumber'))
        Assert.assertEquals("1001", (result.get('urn:ietf:params:scim:schemas:extension:enterprise:2.0:User') as Map).get('employeeNumber'))
    }

    @Test
    void testMapEntireResource() {
        MappingContext context = new MappingContext(data, schema, [], [])
        Configuration configuration = Configuration.withMapObjectProvider()
        Map result = ResourceMapper.convertToMap(context, configuration)

        Assert.assertTrue(result.containsKey('userName'))
        Assert.assertTrue(result.containsKey('name'))
        Assert.assertTrue(result.containsKey('emails'))
        Assert.assertFalse(result.containsKey('password'))
    }

    @Test
    void testMapIncludedPath() {
        MappingContext context = new MappingContext(data, schema, ['userName', 'emails.value'], [])
        Configuration configuration = Configuration.withMapObjectProvider()
        Map result = ResourceMapper.convertToMap(context, configuration)

        Assert.assertTrue(result.containsKey('userName'))
        Assert.assertFalse(result.containsKey('name'))
        Assert.assertFalse(result.containsKey('password'))
        Assert.assertTrue(result.containsKey('emails'))
        Assert.assertTrue(((result.get('emails') as List)[0] as Map).containsKey('value'))
        Assert.assertFalse(((result.get('emails') as List)[0] as Map).containsKey('primary'))
    }

    @Test
    void testMapExcludePath() {
        MappingContext context = new MappingContext(data, schema, [], ['name', 'emails.primary'])
        Configuration configuration = Configuration.withMapObjectProvider()
        Map result = ResourceMapper.convertToMap(context, configuration)

        Assert.assertTrue(result.containsKey('userName'))
        Assert.assertFalse(result.containsKey('name'))
        Assert.assertFalse(result.containsKey('password'))
        Assert.assertTrue(result.containsKey('emails'))
        Assert.assertTrue(((result.get('emails') as List)[0] as Map).containsKey('value'))
        Assert.assertFalse(((result.get('emails') as List)[0] as Map).containsKey('primary'))
        Assert.assertTrue(((result.get('emails') as List)[1] as Map).containsKey('value'))
        Assert.assertFalse(((result.get('emails') as List)[1] as Map).containsKey('primary'))
    }
}
