package com.kumiq.identity.scim.resource.misc

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.core.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static com.kumiq.identity.scim.resource.constant.ScimConstants.RESOURCE_TYPE_RESOURCE_TYPE
import static com.kumiq.identity.scim.resource.constant.ScimConstants.URN_RESOURCE_TYPE

/**
 * Resource type resource
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
final class ResourceType extends Resource {

    ResourceType() {
        this.schemas = [URN_RESOURCE_TYPE]
        this.meta = new Meta(resourceType: RESOURCE_TYPE_RESOURCE_TYPE)
    }

    @JsonProperty('name')
    String name

    @JsonProperty('description')
    String description

    @JsonProperty('endpoint')
    String endpoint

    @JsonProperty('schema')
    String schema

    @JsonProperty('schemaExtensions')
    List<SchemaExtension> schemaExtensions = []

    @ToString
    @EqualsAndHashCode
    static class SchemaExtension {
        @JsonProperty('schema') String schema
        @JsonProperty('required') boolean required
    }
}
