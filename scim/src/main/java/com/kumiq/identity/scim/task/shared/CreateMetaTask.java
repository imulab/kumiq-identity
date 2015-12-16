package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Meta;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CreateMetaTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    @Value("${server.baseUrl:http://localhost:8080}")
    private String serverBaseUrl;

    private String resourceType;

    private String resourceUriSegment;

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource().getId());

        Meta meta = new Meta();
        meta.setResourceType(resourceType);
        Date creationTime = new Date();
        meta.setCreated(creationTime);
        meta.setLastModified(creationTime);
        meta.setLocation(serverBaseUrl + resourceUriSegment + context.getResource().getId());
        meta.setVersion(1l);

        context.getResource().setMeta(meta);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(resourceType);
        Assert.notNull(resourceUriSegment);
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUriSegment() {
        return resourceUriSegment;
    }

    public void setResourceUriSegment(String resourceUriSegment) {
        this.resourceUriSegment = resourceUriSegment;
    }
}
