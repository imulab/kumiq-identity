package com.kumiq.identity.scim.task.group.get;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.GroupGetContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.exception.ExceptionFactory;

/**
 * Ensure the group found in previous tasks actually exists (Separated for modularity)
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class AssertGroupExistsTask<T extends Group> implements Task<GroupGetContext<T>> {

    @Override
    public void perform(GroupGetContext<T> context) {
        if (context.getResource() == null)
            throw ExceptionFactory.groupResourceNotFound(context.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
