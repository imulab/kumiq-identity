package com.kumiq.identity.scim.resource.core

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Base for all SCIM resource, contains elements in core schema.
 *
 * @author Weinan Qiu
 * @since 0.1.0
 */
abstract class Resource {

    @JsonProperty('schemas')
    List<String> schemas = []

    @JsonProperty('id')
    String id

    @JsonProperty('externalId')
    String externalId

    @JsonProperty('meta')
    Meta meta
}
