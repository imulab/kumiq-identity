package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.PatchContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CopyOriginalToResourceTask<T extends Resource> implements Task<PatchContext<T>> {

    @Override
    public void perform(PatchContext<T> context) {
        Assert.notNull(context.getOriginalCopy());

        context.setResource(context.getOriginalCopy());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
