package com.kumiq.identity.scim.resource.misc

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.core.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static com.kumiq.identity.scim.resource.constant.ScimConstants.RESOURCE_TYPE_SCHEMA
import static com.kumiq.identity.scim.resource.constant.ScimConstants.URN_SCHEMA

/**
 * Schema resource
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
final class Schema extends Resource {

    Schema() {
        this.schemas = [URN_SCHEMA]
        this.meta = new Meta(resourceType: RESOURCE_TYPE_SCHEMA)
    }

    @JsonProperty('name')
    String name

    @JsonProperty('description')
    String description

    @JsonProperty('attributes')
    List<Attribute> attributes = []

    @ToString
    @EqualsAndHashCode
    static class Attribute {
        @JsonProperty('name') String name
        @JsonProperty('description') String description
        @JsonProperty('type') String type
        @JsonProperty('mutability') String mutability
        @JsonProperty('returned') String returned
        @JsonProperty('uniqueness') String uniqueness
        @JsonProperty('required') boolean required
        @JsonProperty('multiValued') boolean multiValued
        @JsonProperty('caseExact') boolean caseExact
        @JsonProperty('canonicalValues') List<String> canonicalValues = []
        @JsonProperty('referenceTypes') List<String> referenceTypes = []
        @JsonProperty('subAttributes') List<Attribute> subAttributes = []
    }
}