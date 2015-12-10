package com.kumiq.identity.scim.database;

import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.misc.ResourceType;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.resource.misc.ServiceProviderConfig;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ResourceDatabase<T extends Resource> {

    Optional<T> findById(String id);

    List<T> findAll();

    T save(T resource);

    void delete(T resource);

    default int count() {
        List<T> all = findAll();
        return CollectionUtils.isEmpty(all) ? 0 : all.size();
    }

    default boolean exists(String id) {
        return findById(id).isPresent();
    }

    /**
     * User resource database
     * @param <T> user class
     */
    interface UserDatabase<T extends User> extends ResourceDatabase<T> {

        Optional<T> findByUserName(String userName);

        List<T> query(String filter, String sort, boolean ascending);
    }

    /**
     * Group resource database
     * @param <T> group class
     */
    interface GroupDatabase<T extends Group> extends ResourceDatabase<T> {

        List<T> findGroupsWithMember(String memberId, boolean transitive);

        void deleteMember(String memberId);
    }

    /**
     * Resource type database
     */
    interface ResourceTypeDatabase extends ResourceDatabase<ResourceType> {

    }

    /**
     * Schema database
     */
    interface SchemaDatabase extends ResourceDatabase<Schema> {

    }

    /**
     * Service provider config database
     */
    interface ServiceProviderConfigDatabase extends ResourceDatabase<ServiceProviderConfig> {

        default ServiceProviderConfig find() {
            Optional<ServiceProviderConfig> result = findById(ServiceProviderConfig.SINGLETON_ID);
            if (!result.isPresent())
                throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_SERVICE_PROVIDER_CONFIG, ServiceProviderConfig.SINGLETON_ID);
            return result.get();
        }

        default boolean exists() {
            return findById(ServiceProviderConfig.SINGLETON_ID).isPresent();
        }
    }
}
