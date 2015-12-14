package com.kumiq.identity.scim.resource.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.extension.EnterpriseExtension

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
class ScimUser extends User implements EnterpriseExtension {

    @JsonProperty('urn:ietf:params:scim:schemas:extension:enterprise:2.0:User')
    EnterpriseExtension enterpriseExtension
}
