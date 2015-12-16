package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class SaveResourceTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    protected abstract ResourceDatabase<T> resourceDatabase();

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource());

        resourceDatabase().save(context.getResource());
    }
}
