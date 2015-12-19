package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.path.ModificationUnit
import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User
import com.kumiq.identity.scim.task.shared.FormalizeModificationValueTask
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
class FormalizeModificationValueTaskTests {

    FormalizeModificationValueTask task
    Schema testSchema

    @Before
    void setup() {
        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'name', type: ScimConstants.ATTR_TYPES_COMPLEX, multiValued: false, clazz: User.Name),
                new Schema.Attribute(name: 'emails', type: ScimConstants.ATTR_TYPES_COMPLEX, multiValued: true, clazz: ArrayList, elementClazz: User.Email),
                new Schema.Attribute(name: 'phoneNumbers', type: ScimConstants.ATTR_TYPES_COMPLEX, multiValued: true, clazz: ArrayList, elementClazz: User.PhoneNumber),
        ])
        task = new FormalizeModificationValueTask()
    }

    @Test
    void testFormalizeComplexAttribute() {
        PatchContext context = new UserPatchContext(modifications: [
                new ModificationUnit(operation: ModificationUnit.Operation.REPLACE,
                        path: 'name',
                        value: ['givenName': 'Weinan', 'familyName': 'Qiu']),
                new ModificationUnit(operation: ModificationUnit.Operation.ADD,
                        path: 'emails',
                        value: ['value': 'foo@bar.com', 'type': 'work', 'primary': true]),
                new ModificationUnit(operation: ModificationUnit.Operation.ADD,
                        path: 'phoneNumbers',
                        value: [
                                ['value': '123456', 'primary': true],
                                ['value': '54321', 'primary': true],
                        ])
        ])
        context.setSchema(testSchema)
        task.perform(context)

        Assert.assertTrue(context.modifications[0].value.class == User.Name)
        Assert.assertTrue(context.modifications[1].value.class == User.Email)
        Assert.assertTrue((context.modifications[2].value as List)[0].class == User.PhoneNumber)
        Assert.assertTrue((context.modifications[2].value as List)[1].class == User.PhoneNumber)
    }
}
