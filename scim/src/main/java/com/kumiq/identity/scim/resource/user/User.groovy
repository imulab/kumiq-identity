package com.kumiq.identity.scim.resource.user

import com.fasterxml.jackson.annotation.JsonProperty
import com.kumiq.identity.scim.resource.core.Meta
import com.kumiq.identity.scim.resource.core.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import static com.kumiq.identity.scim.resource.constant.ScimConstants.RESOURCE_TYPE_USER
import static com.kumiq.identity.scim.resource.constant.ScimConstants.URN_USER

/**
 * User resource
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@ToString
@EqualsAndHashCode
class User extends Resource {

    User() {
        this.schemas = [URN_USER]
        this.meta = new Meta(resourceType: RESOURCE_TYPE_USER)
    }

    @JsonProperty('userName')
    String userName

    @JsonProperty('name')
    Name name

    @JsonProperty('displayName')
    String displayName

    @JsonProperty('nickName')
    String nickName

    @JsonProperty('profileUrl')
    String profileUrl

    @JsonProperty('title')
    String title

    @JsonProperty('userType')
    String userType

    @JsonProperty('preferredLanguage')
    String preferredLanguage

    @JsonProperty('locale')
    String locale

    @JsonProperty('timezone')
    String timezone

    @JsonProperty('active')
    boolean active

    @JsonProperty('password')
    String password

    @JsonProperty('emails')
    List<Email> emails = []

    @JsonProperty('phoneNumbers')
    List<PhoneNumber> phoneNumbers = []

    @JsonProperty('ims')
    List<IMS> ims = []

    @JsonProperty('photos')
    List<Photo> photos = []

    @JsonProperty('addresses')
    List<Address> addresses = []

    @JsonProperty('groups')
    List<Group> groups = []

    @JsonProperty('entitlements')
    List<Entitlement> entitlements = []

    @JsonProperty('roles')
    List<Role> roles = []

    @JsonProperty('x509Certificates')
    List<X509Certificate> x509Certificates = []

    @ToString
    @EqualsAndHashCode
    static class Name {
        @JsonProperty('formatted') String formatted
        @JsonProperty('familyName') String familyName
        @JsonProperty('givenName') String givenName
        @JsonProperty('middleName') String middleName
        @JsonProperty('honorificPrefix') String honorificPrefix
        @JsonProperty('honorificSuffix') String honorificSuffix
    }

    @ToString
    @EqualsAndHashCode
    static class Email {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class PhoneNumber {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class IMS {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class Photo {
        @JsonProperty('value') String value
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class Address {
        @JsonProperty('formatted') String formatted
        @JsonProperty('streetAddress') String streetAddress
        @JsonProperty('locality') String locality
        @JsonProperty('region') String region
        @JsonProperty('postalCode') String postalCode
        @JsonProperty('country') String country
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class Group {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('$ref') String $ref
        @JsonProperty('type') String type
    }

    @ToString
    @EqualsAndHashCode
    static class Entitlement {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class Role {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    @ToString
    @EqualsAndHashCode
    static class X509Certificate {
        @JsonProperty('value') String value
        @JsonProperty('display') String display
        @JsonProperty('type') String type
        @JsonProperty('primary') boolean primary
    }

    boolean hasGroup(String groupId) {
        this.groups.find { it.value == groupId } != null
    }
}
