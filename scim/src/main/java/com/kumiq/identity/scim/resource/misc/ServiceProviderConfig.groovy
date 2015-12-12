package com.kumiq.identity.scim.resource.misc

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.core.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static com.kumiq.identity.scim.resource.constant.ScimConstants.RESOURCE_TYPE_SERVICE_PROVIDER_CONFIG
import static com.kumiq.identity.scim.resource.constant.ScimConstants.URN_SERVICE_PROVIDER_CONFIG

/**
 * Service provider configuration
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
final class ServiceProviderConfig extends Resource {

    public static final String SINGLETON_ID = 'ServiceProviderConfig'

    ServiceProviderConfig() {
        this.schemas = [URN_SERVICE_PROVIDER_CONFIG]
        this.meta = new Meta(resourceType: RESOURCE_TYPE_SERVICE_PROVIDER_CONFIG)
    }

    @JsonProperty('documentationUri')
    String documentationUri

    @JsonProperty('patch')
    Patch patch

    @JsonProperty('bulk')
    Bulk bulk

    @JsonProperty('filter')
    Filter filter

    @JsonProperty('etag')
    ETag etag

    @JsonProperty('changePassword')
    ChangePassword changePassword

    @JsonProperty('sort')
    Sort sort

    @JsonProperty('authenticationSchemes')
    List<AuthenticationScheme> authenticationSchemes = []

    @ToString
    @EqualsAndHashCode
    static class Patch {
        @JsonProperty('supported') boolean supported
    }

    @ToString
    @EqualsAndHashCode
    static class Bulk {
        @JsonProperty('supported') boolean supported
        @JsonProperty('maxOperations') int maxOperations
        @JsonProperty('maxPayloadSize') int maxPayloadSize
    }

    @ToString
    @EqualsAndHashCode
    static class Filter {
        @JsonProperty('supported') boolean supported
        @JsonProperty('maxResults') int maxResults
    }

    @ToString
    @EqualsAndHashCode
    static class ChangePassword {
        @JsonProperty('supported') boolean supported
    }

    @ToString
    @EqualsAndHashCode
    static class Sort {
        @JsonProperty('supported') boolean supported
    }

    @ToString
    @EqualsAndHashCode
    static class ETag {
        @JsonProperty('supported') boolean supported
    }

    @ToString
    @EqualsAndHashCode
    static class AuthenticationScheme {
        @JsonProperty('type') String type
        @JsonProperty('name') String name
        @JsonProperty('description') String description
        @JsonProperty('specUri') String specUri
        @JsonProperty('documentationUri') String documentationUri
        @JsonProperty('primary') boolean primary
    }
}
