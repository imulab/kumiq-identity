package com.kumiq.identity.scim.path

import com.kumiq.identity.scim.mapper.MappingContext
import com.kumiq.identity.scim.resource.constant.ScimConstants
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
class MappingContextTests {

    @Test
    void createContextWithoutInclusionPaths() {
        Schema schema = new Schema(attributes: [
                new Schema.Attribute(name: 'foo1', returned: ScimConstants.RETURNED_ALWAYS),
                new Schema.Attribute(name: 'foo2', returned: ScimConstants.RETURNED_NEVER),
                new Schema.Attribute(name: 'foo3', returned: ScimConstants.RETURNED_DEFAULT),
                new Schema.Attribute(name: 'foo4', returned: ScimConstants.RETURNED_REQUEST)
        ])
        MappingContext context = new MappingContext([:], [schema], [], [])
        Assert.assertTrue(context.getIncludePaths().contains('foo1'))
        Assert.assertFalse(context.getIncludePaths().contains('foo2'))
        Assert.assertTrue(context.getIncludePaths().contains('foo3'))
        Assert.assertTrue(context.getIncludePaths().contains('foo4'))
    }

    @Test
    void createContextWithNonAttributePath() {
        Schema schema = new Schema(attributes: [
                new Schema.Attribute(name: 'foo1', returned: ScimConstants.RETURNED_ALWAYS),
                new Schema.Attribute(name: 'foo2', returned: ScimConstants.RETURNED_NEVER),
                new Schema.Attribute(name: 'foo3', returned: ScimConstants.RETURNED_DEFAULT),
                new Schema.Attribute(name: 'foo4', returned: ScimConstants.RETURNED_REQUEST)
        ])
        MappingContext context = new MappingContext([:], [schema], ['foo5'], [])
        Assert.assertFalse(context.getIncludePaths().contains('foo5'))
    }

    @Test
    void createContextWithAlwaysReturnExclusion() {
        Schema schema = new Schema(attributes: [
                new Schema.Attribute(name: 'foo1', returned: ScimConstants.RETURNED_ALWAYS),
                new Schema.Attribute(name: 'foo2', returned: ScimConstants.RETURNED_NEVER),
                new Schema.Attribute(name: 'foo3', returned: ScimConstants.RETURNED_DEFAULT),
                new Schema.Attribute(name: 'foo4', returned: ScimConstants.RETURNED_REQUEST)
        ])
        MappingContext context = new MappingContext([:], [schema], [], ['foo1'])
        Assert.assertFalse(context.getExcludePaths().contains('foo1'))
    }
}
