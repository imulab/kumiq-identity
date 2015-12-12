package com.kumiq.identity.scim.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.misc.ResourceType;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.resource.misc.ServiceProviderConfig;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
abstract public class GenericResourceInitialization<T extends Resource> implements ResourceInitialization<T> {

    private String filePath;
    private ObjectMapper objectMapper;
    private ResourceDatabase<T> database;
    private Class<? extends ResourceInitWrapper> wrapperClass;

    @Override
    @SuppressWarnings("unchecked")
    public List<T> bootstrap() {
        try {
            return objectMapper.readValue(new ClassPathResource(filePath).getInputStream(), wrapperClass).getResources();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<T> postProcess(List<T> resources) {
        return resources;
    }

    @Override
    public void install(List<T> resources) {
        resources.forEach(t -> {
            Assert.notNull(t.getId());
            if (database.exists(t.getId()))
                throw ExceptionFactory.resourceAlreadyExists(resourceTypeLookup(t), t.getId());
            else
                database.save(t);
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.isTrue(new ClassPathResource(filePath).exists());
        Assert.notNull(database);
        if (objectMapper == null)
            objectMapper = new ObjectMapper();

        install(postProcess(bootstrap()));
    }

    private String resourceTypeLookup(T resource) {
        if (User.class.isAssignableFrom(resource.getClass()))
            return ScimConstants.RESOURCE_TYPE_USER;
        else if (Group.class.isAssignableFrom(resource.getClass()))
            return ScimConstants.RESOURCE_TYPE_GROUP;
        else if (ServiceProviderConfig.class.equals(resource.getClass()))
            return ScimConstants.RESOURCE_TYPE_SERVICE_PROVIDER_CONFIG;
        else if (Schema.class.equals(resource.getClass()))
            return ScimConstants.RESOURCE_TYPE_SCHEMA;
        else if (ResourceType.class.equals(resource.getClass()))
            return ScimConstants.RESOURCE_TYPE_RESOURCE_TYPE;
        else
            return ScimConstants.RESOURCE_TYPE_UNKNOWN;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResourceDatabase<T> getDatabase() {
        return database;
    }

    public void setDatabase(ResourceDatabase<T> database) {
        this.database = database;
    }

    public Class<? extends ResourceInitWrapper> getWrapperClass() {
        return wrapperClass;
    }

    public void setWrapperClass(Class<? extends ResourceInitWrapper> wrapperClass) {
        this.wrapperClass = wrapperClass;
    }

    public static class UserInit<T extends User> extends GenericResourceInitialization<T> {}

    public static class GroupInit<T extends Group> extends GenericResourceInitialization<T> {}

    public static class SchemaInit extends GenericResourceInitialization<Schema> {}

    public static class ResourceTypeInit extends GenericResourceInitialization<ResourceType> {}

    public static class ServiceProviderConfigInit extends GenericResourceInitialization<ServiceProviderConfig> {

        @Override
        public List<ServiceProviderConfig> postProcess(List<ServiceProviderConfig> resources) {
            Assert.isTrue(resources.size() <= 1, "Cannot bootstrap more than 1 service provider config");
            resources.get(0).setId(ServiceProviderConfig.SINGLETON_ID);
            return resources;
        }
    }
}
