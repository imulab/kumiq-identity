package com.kumiq.identity.scim.resource.extension

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
trait EnterpriseExtension {

    static class Extension {
        @JsonProperty('employeeNumber')
        String employeeNumber

        @JsonProperty('costCenter')
        String costCenter

        @JsonProperty('organization')
        String organization

        @JsonProperty('division')
        String division

        @JsonProperty('department')
        String department

        @JsonProperty('manager')
        Manager manager

        static class Manager {
            @JsonProperty('value') String value
            @JsonProperty('displayName') String displayName
            @JsonProperty('$ref') String $ref
        }
    }
}