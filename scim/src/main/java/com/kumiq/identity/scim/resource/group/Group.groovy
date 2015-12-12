package com.kumiq.identity.scim.resource.group

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.core.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static com.kumiq.identity.scim.resource.constant.ScimConstants.*

/**
 * Group resource
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
class Group extends Resource {

    Group() {
        this.schemas = [URN_GROUP]
        this.meta = new Meta(resourceType: RESOURCE_TYPE_GROUP)
    }

    @JsonProperty('displayName')
    String displayName

    @JsonProperty('members')
    List<Member> members = []

    @ToString
    @EqualsAndHashCode
    static class Member {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('$ref') String $ref
    }

    boolean hasMember(String memberId) {
        members.find { it.value == memberId } != null
    }
}
