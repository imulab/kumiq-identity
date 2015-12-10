package com.kumiq.identity.scim.init;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.misc.ResourceType;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.resource.misc.ServiceProviderConfig;
import com.kumiq.identity.scim.resource.user.User;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
abstract public class ResourceInitWrapper<T extends Resource> {

    @JsonProperty("resources")
    private List<T> resources;

    public List<T> getResources() {
        return resources;
    }

    public void setResources(List<T> resources) {
        this.resources = resources;
    }

    final public static class UserResourceInitWrapper extends ResourceInitWrapper<User> {

    }

    final public static class GroupResourceInitWrapper extends ResourceInitWrapper<Group> {

    }

    final public static class ResourceTypeResourceInitWrapper extends ResourceInitWrapper<ResourceType> {

    }

    final public static class SchemaResourceInitWrapper extends ResourceInitWrapper<Schema> {

    }

    final public static class ServiceProviderConfigResourceInitWrapper extends ResourceInitWrapper<ServiceProviderConfig> {

    }
}
