package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.resource.constant.ScimConstants
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.task.group.replace.CheckGroupMutabilityTask
import com.kumiq.identity.scim.task.shared.CheckMutabilityTask
import com.kumiq.identity.scim.utils.ExceptionFactory.ResourceImmutabilityViolatedException
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
class CheckMutabilityTaskTests {

    CheckMutabilityTask task
    Schema testSchema

    @Before
    void setup() {
        task = new CheckGroupMutabilityTask()
        testSchema = new Schema(attributes: [
                new Schema.Attribute(name: 'displayName', mutability: ScimConstants.MUTABILITY_READWRITE),
                new Schema.Attribute(name: 'members', mutability: ScimConstants.MUTABILITY_READWRITE, multiValued: true, subAttributes: [
                        new Schema.Attribute(name: 'value', mutability: ScimConstants.MUTABILITY_IMMUTABLE)
                ])
        ])
    }

    @Test
    void testViolateImmutableConstraint() {
        Group original = new Group(displayName: 'group1', members: [
                new Group.Member(value: '12345'),
                new Group.Member(value: '54321')
        ])
        Group resource = new Group(displayName: 'group1-alt', members: [
                new Group.Member(value: '123456'),
                new Group.Member(value: '54321')
        ])
        ReplaceContext context = new GroupReplaceContext(originalCopy: original, resource: resource, schema: testSchema)

        try {
            task.perform(context)
            Assert.fail('test should have failed!')
        } catch (ResourceImmutabilityViolatedException ex) {
            Assert.assertEquals('members.value', ex.violatedPath)
        }
    }

    @Test
    void testAddItemToComplexWritableAttributeWhereSubAttributesAreImmutable() {
        Group original = new Group(displayName: 'group1', members: [
                new Group.Member(value: '12345'),
                new Group.Member(value: '54321')
        ])
        Group resource = new Group(displayName: 'group1-alt', members: [
                new Group.Member(value: '12345'),
                new Group.Member(value: '54321'),
                new Group.Member(value: '123456')
        ])
        ReplaceContext context = new GroupReplaceContext(originalCopy: original, resource: resource, schema: testSchema)
        task.perform(context)
    }

    @Test
    void testRemoveItemToComplexWritableAttributeWhereSubAttributesAreImmutable() {
        Group original = new Group(displayName: 'group1', members: [
                new Group.Member(value: '12345'),
                new Group.Member(value: '54321')
        ])
        Group resource = new Group(displayName: 'group1-alt', members: [
                new Group.Member(value: '12345')
        ])
        ReplaceContext context = new GroupReplaceContext(originalCopy: original, resource: resource, schema: testSchema)
        task.perform(context)
    }
}
