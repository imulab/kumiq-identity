package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.misc.ResourceType;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping("/ResourceTypes")
public class ResourceTypeEndpoints {

    @Autowired
    ResourceDatabase.ResourceTypeDatabase database;

    @RequestMapping(method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public List<ResourceType> retrieveAllResourceTypes() {
        List<ResourceType> allResourceTypes = database.findAll();
        if (CollectionUtils.isEmpty(allResourceTypes))
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_RESOURCE_TYPE, "*");
        return allResourceTypes;
    }

    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public ResourceType retrieveResourceTypeById(@PathVariable String resourceId) {
        Optional<ResourceType> resourceType = database.findById(resourceId);
        if (!resourceType.isPresent())
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_RESOURCE_TYPE, resourceId);
        return resourceType.get();
    }
}
