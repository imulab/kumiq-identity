package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class UpdateMetaTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource().getMeta());

        context.getResource().getMeta().setLastModified(new Date());
        context.getResource().getMeta().setVersion(context.getResource().getMeta().getVersion() + 1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
