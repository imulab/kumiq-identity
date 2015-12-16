package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AssignIdTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    private String idPrefix = "";

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource());
        String id = idPrefix + UUID.randomUUID().toString();
        context.getResource().setId(id);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(idPrefix);
    }

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }
}
