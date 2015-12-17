package com.kumiq.identity.scim.task.group.replace;

import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.task.shared.CheckMutabilityTask;
import com.kumiq.identity.scim.utils.ExceptionFactory;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckGroupMutabilityTask<T extends Group> extends CheckMutabilityTask<T> {

    @Override
    protected ExceptionFactory.ResourceImmutabilityViolatedException violatedException(String path, String resourceId) {
        return ExceptionFactory.groupImmutabilityViolated(path, resourceId);
    }
}
