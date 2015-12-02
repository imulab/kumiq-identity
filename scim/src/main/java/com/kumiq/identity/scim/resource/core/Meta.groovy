package com.kumiq.identity.scim.resource.core

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.kumiq.identity.scim.utils.JsonDateSerializer
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Meta section of core schema
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
class Meta {

    @JsonProperty('resourceType')
    String resourceType

    @JsonProperty('created')
    @JsonSerialize(using = JsonDateSerializer)
    Date created

    @JsonProperty('lastModified')
    @JsonSerialize(using = JsonDateSerializer)
    Date lastModified

    @JsonProperty('location')
    String location

    @JsonProperty('version')
    Long version
}
